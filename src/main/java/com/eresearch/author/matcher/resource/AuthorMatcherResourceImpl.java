package com.eresearch.author.matcher.resource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.codahale.metrics.Timer;
import com.eresearch.author.matcher.dto.AuthorComparisonDto;
import com.eresearch.author.matcher.dto.AuthorMatcherResultsDto;
import com.eresearch.author.matcher.exception.DataValidationException;
import com.eresearch.author.matcher.metrics.entries.ResourceLayerMetricEntry;
import com.eresearch.author.matcher.service.AuthorMatcherService;
import com.eresearch.author.matcher.validator.Validator;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/author-matcher")
@Log4j
public class AuthorMatcherResourceImpl implements AuthorMatcherResource {

    private static final Long DEFERRED_RESULT_TIMEOUT = TimeUnit.MILLISECONDS.toMinutes(7);

    @Qualifier("authorMatcherExecutor")
    @Autowired
    public ExecutorService authorMatcherExecutor;

    @Autowired
    private AuthorMatcherService authorMatcherService;

    @Autowired
    private Validator<AuthorComparisonDto> authorComparisonDtoValidator;

    @Autowired
    private ResourceLayerMetricEntry resourceLayerMetricEntry;

    @RequestMapping(method = RequestMethod.POST, path = "/match", consumes = {
            MediaType.APPLICATION_JSON_UTF8_VALUE }, produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
    @Override
    public @ResponseBody DeferredResult<AuthorMatcherResultsDto> authorMatcherOperation(
            @RequestBody AuthorComparisonDto authorComparisonDto) {

        DeferredResult<AuthorMatcherResultsDto> deferredResult = new DeferredResult<>(DEFERRED_RESULT_TIMEOUT);

        Runnable task = authorMatcherOperation(authorComparisonDto, deferredResult);
        authorMatcherExecutor.execute(task);

        return deferredResult;
    }

    private Runnable authorMatcherOperation(AuthorComparisonDto authorComparisonDto, DeferredResult<AuthorMatcherResultsDto> deferredResult) {

        return () -> {

            Timer.Context context = resourceLayerMetricEntry.getResourceLayerTimer().time();
            try {

                authorComparisonDtoValidator.validate(authorComparisonDto);

                AuthorMatcherResultsDto authorMatcherResultsDto = authorMatcherService.authorMatcherOperation(authorComparisonDto);

                resourceLayerMetricEntry.getSuccessResourceLayerCounter().inc();

                deferredResult.setResult(authorMatcherResultsDto);

            } catch (DataValidationException e) {

                log.error("AuthorMatcherResourceImpl#authorMatcherOperation --- error occurred.", e);

                resourceLayerMetricEntry.getFailureResourceLayerCounter().inc();

                deferredResult.setErrorResult(e);

            } finally {
                context.stop();
            }
        };

    }
}
