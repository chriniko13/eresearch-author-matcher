package com.eresearch.author.matcher;

import com.eresearch.author.matcher.application.event.listener.*;
import com.eresearch.author.matcher.db.DbOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class EresearchAuthorMatcherApplication implements CommandLineRunner, ApplicationRunner {

    public static void main(String[] args) {

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(
                EresearchAuthorMatcherApplication.class);

        registerApplicationListeners(springApplicationBuilder);

        springApplicationBuilder
                .web(true)
                .run(args);
    }

    private static void registerApplicationListeners(final SpringApplicationBuilder springApplicationBuilder) {

        springApplicationBuilder.listeners(new ApplicationEnvironmentPreparedEventListener());
        springApplicationBuilder.listeners(new ApplicationFailedEventListener());
        springApplicationBuilder.listeners(new ApplicationReadyEventListener());
        springApplicationBuilder.listeners(new ApplicationStartedEventListener());
        springApplicationBuilder.listeners(new BaseApplicationEventListener());

    }

    @Autowired
    private DbOperations dbOperations;

    @Override
    public void run(String... strings) throws Exception {
        dbOperations.runTask();
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        //NOTE: add your scenarios if needed.
    }
}
