package com.eresearch.author.matcher.application.actuator.health;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.DataSourceHealthIndicator;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicator;
import org.springframework.boot.actuate.health.DiskSpaceHealthIndicatorProperties;
import org.springframework.boot.actuate.health.Health;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.eresearch.author.matcher.dao.AuthorMatcherDao;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class EresearchAuthorMatcherHealthCheck extends AbstractHealthIndicator {

    @Qualifier("hikariDataSource")
    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    private AuthorMatcherDao authorMatcherDao;

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {

        this.performBasicHealthChecks();

        Optional<Exception> ex = this.specificHealthCheck();

        if (ex.isPresent()) {
            builder.down(ex.get());
        } else {
            builder.up();
        }
    }

    private void performBasicHealthChecks() {
        //check disk...
        DiskSpaceHealthIndicatorProperties diskSpaceHealthIndicatorProperties
                = new DiskSpaceHealthIndicatorProperties();
        diskSpaceHealthIndicatorProperties.setThreshold(10737418240L); /*10 GB*/
        new DiskSpaceHealthIndicator(diskSpaceHealthIndicatorProperties);

        //check datasource...
        new DataSourceHealthIndicator(hikariDataSource);
    }

    private Optional<Exception> specificHealthCheck() {

        //check if required table(s) exist...
        Optional<Exception> ex1 = this.specificDbHealthCheck();
        if (ex1.isPresent()) {
            return ex1;
        }

        return Optional.empty();
    }

    private Optional<Exception> specificDbHealthCheck() {
        if (Objects.isNull(hikariDataSource)) {
            log.error("EresearchAuthorMatcherHealthCheck#specificDbHealthCheck --- hikariDataSource is null.");
            return Optional.of(new NullPointerException("hikariDataSource is null."));
        }

        JdbcTemplate jdbcTemplate = new JdbcTemplate(hikariDataSource);

        try {
            jdbcTemplate.execute(authorMatcherDao.getSelectQueryForSearchResultsTable());
        } catch (DataAccessException ex) {
            log.error("EresearchAuthorMatcherHealthCheck#specificDbHealthCheck --- db is in bad state.", ex);
            return Optional.of(ex);
        }

        return Optional.empty();
    }
}
