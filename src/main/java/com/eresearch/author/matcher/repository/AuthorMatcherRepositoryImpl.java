package com.eresearch.author.matcher.repository;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.codahale.metrics.Timer;
import com.eresearch.author.matcher.dao.AuthorMatcherDao;
import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.eresearch.author.matcher.dto.AuthorMatcherResultsDto;
import com.eresearch.author.matcher.metrics.entries.RepositoryLayerMetricEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j;

@Log4j
@Repository
public class AuthorMatcherRepositoryImpl implements AuthorMatcherRepository {

    @Autowired
    private AuthorMatcherDao authorMatcherDao;

    @Autowired
    private Clock clock;

    @Autowired
    @Qualifier("elsevierObjectMapper")
    private ObjectMapper objectMapper;

    @Autowired
    @Qualifier("dbOperationsExecutor")
    private ExecutorService dbOperationsExecutor;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Qualifier("transactionTemplate")
    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private RepositoryLayerMetricEntry repositoryLayerMetricEntry;

    @Override
    public void save(AuthorComparisonDto authorComparisonDto, AuthorMatcherResultsDto authorMatcherResultsDto) {

        Runnable task = performSaveTask(authorComparisonDto, authorMatcherResultsDto);
        CompletableFuture.runAsync(task, dbOperationsExecutor);

    }

    private Runnable performSaveTask(AuthorComparisonDto authorComparisonDto, AuthorMatcherResultsDto authorMatcherResultsDto) {

        return () -> {

            Timer.Context context = repositoryLayerMetricEntry.getRepositoryLayerTimer().time();
            try {

                final String sqlToExecute = authorMatcherDao.getInsertQueryForSearchResultsTable();

                String authorComparisonDtoStr = objectMapper.writeValueAsString(authorComparisonDto);
                String authorMatcherResultsDtoStr = objectMapper.writeValueAsString(authorMatcherResultsDto);
                Timestamp creationTimestamp = Timestamp.from(Instant.now(clock));

                executeSaveStatement(sqlToExecute, authorComparisonDtoStr, authorMatcherResultsDtoStr, creationTimestamp);

            } catch (JsonProcessingException e) {

                log.error("AuthorMatcherRepositoryImpl#save --- error occurred --- not even tx initialized.", e);
            } finally {
                context.stop();
            }

        };
    }

    private void executeSaveStatement(final String sqlToExecute,
                                      final String authorComparisonDtoStr,
                                      final String authorMatcherResultsDtoStr,
                                      final Timestamp creationTimestamp) {

        transactionTemplate.execute(new TransactionCallbackWithoutResult() {

            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                try {
                    jdbcTemplate.update(sqlToExecute,
                            authorComparisonDtoStr,
                            authorMatcherResultsDtoStr,
                            creationTimestamp);
                } catch (DataAccessException e) {
                    log.error("AuthorMatcherRepositoryImpl#save --- error occurred --- proceeding with rollback.", e);
                    transactionStatus.setRollbackOnly();
                }

            }
        });
    }

}
