package com.eresearch.author.matcher.exception;


import com.eresearch.author.matcher.error.EresearchAuthorMatcherError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DataValidationException extends Exception {

	private static final long serialVersionUID = -4807690929767265288L;

	private final EresearchAuthorMatcherError eresearchAuthorMatcherError;

	public DataValidationException(EresearchAuthorMatcherError eresearchAuthorMatcherError, String message) {
		super(message);
		this.eresearchAuthorMatcherError = eresearchAuthorMatcherError;
	}

	public EresearchAuthorMatcherError getEresearchAuthorMatcherError() {
		return eresearchAuthorMatcherError;
	}
}
