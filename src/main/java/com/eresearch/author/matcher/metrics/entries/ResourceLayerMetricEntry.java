package com.eresearch.author.matcher.metrics.entries;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.eresearch.author.matcher.resource.AuthorMatcherResource;

@Component
public class ResourceLayerMetricEntry {

    @Qualifier("appMetricRegistry")
    @Autowired
    private MetricRegistry metricRegistry;

    private Counter successResourceLayerCounter;

    private Counter failureResourceLayerCounter;

    private Timer resourceLayerTimer;

    @PostConstruct
    public void init() {
        registerCounters();
        registerTimers();
    }

    private void registerTimers() {

        String timerName = MetricRegistry.name(AuthorMatcherResource.class, "authorMatcherOperation", "timer");
        resourceLayerTimer = metricRegistry.timer(timerName);
    }

    private void registerCounters() {

        String counterSuccessName = MetricRegistry.name(AuthorMatcherResource.class,
                "authorMatcherOperation", "counter", "success");

        String counterFailureName = MetricRegistry.name(AuthorMatcherResource.class,
                "authorMatcherOperation", "counter", "failure");

        successResourceLayerCounter = metricRegistry.counter(counterSuccessName);
        failureResourceLayerCounter = metricRegistry.counter(counterFailureName);
    }

    public Counter getFailureResourceLayerCounter() {
        return failureResourceLayerCounter;
    }

    public Counter getSuccessResourceLayerCounter() {
        return successResourceLayerCounter;
    }

    public Timer getResourceLayerTimer() {
        return resourceLayerTimer;
    }
}
