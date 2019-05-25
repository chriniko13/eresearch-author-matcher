package com.eresearch.author.matcher.application.actuator.health;

import lombok.extern.log4j.Log4j;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicator;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicatorProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Log4j
@Component
public class EresearchAuthorMatcherHealthCheck extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        this.performBasicHealthChecks();
        builder.up();
    }

    private void performBasicHealthChecks() {
        //check disk...
        DiskSpaceHealthIndicatorProperties diskSpaceHealthIndicatorProperties
                = new DiskSpaceHealthIndicatorProperties();
        diskSpaceHealthIndicatorProperties.setThreshold(10737418240L); /*10 GB*/
        new DiskSpaceHealthIndicator(diskSpaceHealthIndicatorProperties);
    }
}
