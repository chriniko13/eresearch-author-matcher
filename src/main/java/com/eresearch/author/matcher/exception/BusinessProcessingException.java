package com.eresearch.author.matcher.exception;

import com.eresearch.author.matcher.error.EresearchAuthorMatcherError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class BusinessProcessingException extends Exception {

    private final EresearchAuthorMatcherError eresearchAuthorMatcherError;

    public BusinessProcessingException(EresearchAuthorMatcherError eresearchAuthorMatcherError, String message, Throwable cause) {
        super(message, cause);
        this.eresearchAuthorMatcherError = eresearchAuthorMatcherError;
    }

    public BusinessProcessingException(EresearchAuthorMatcherError eresearchAuthorMatcherError, String message) {
        super(message);
        this.eresearchAuthorMatcherError = eresearchAuthorMatcherError;
    }

    public EresearchAuthorMatcherError getEresearchAuthorMatcherError() {
        return eresearchAuthorMatcherError;
    }
}
