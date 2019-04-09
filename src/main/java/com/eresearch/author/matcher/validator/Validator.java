package com.eresearch.author.matcher.validator;


import com.eresearch.author.matcher.exception.DataValidationException;

public interface Validator<T> {

	void validate(T data) throws DataValidationException;
}
