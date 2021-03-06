package com.eresearch.author.matcher.application.configuration;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.eresearch.author.matcher.deserializer.InstantDeserializer;
import com.eresearch.author.matcher.serializer.InstantSerializer;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.*;

@EnableScheduling

@Configuration
public class AppConfiguration implements SchedulingConfigurer {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(Instant.class, new InstantSerializer());
        javaTimeModule.addDeserializer(Instant.class, new InstantDeserializer());
        objectMapper.registerModule(javaTimeModule);

        return objectMapper;
    }

    /*
     * Handling db operations.
     */
    @Bean(destroyMethod = "shutdownNow")
    @Qualifier("dbOperationsExecutor")
    public ExecutorService dbOperationsExecutor() {
        return new ThreadPoolExecutor(
                20, 120,
                5000L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(300, true),
                new ThreadFactoryBuilder().setNameFormat("db-operations-thread-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Bean
    @Qualifier("elsevierObjectMapper")
    public ObjectMapper elsevierObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); //for elsevier api.
        return objectMapper;
    }

    /*
     * Handling (front) asynchronous communications.
     */
    @Bean(destroyMethod = "shutdownNow")
    @Qualifier("authorMatcherExecutor")
    public ExecutorService authorMatcherExecutor() {
        return new ThreadPoolExecutor(
                20, 120,
                5000L, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(300, true),
                new ThreadFactoryBuilder().setNameFormat("author-matcher-thread-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    @Value("${service.zone.id}")
    private ZoneId zoneId;

    @Bean
    public Clock clock() {
        return Clock.system(zoneId);
    }

    @Bean
    @Qualifier("appMetricRegistry")
    public MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Bean
    public HealthCheckRegistry healthCheckRegistry() {
        return new HealthCheckRegistry();
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newSingleThreadScheduledExecutor());
    }
}
