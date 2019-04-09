package com.eresearch.author.matcher.error;

public enum EresearchAuthorMatcherError {

    // --- validation errors ---
    DATA_VALIDATION_ERROR("Provided data is invalid");

    private String message;

    EresearchAuthorMatcherError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
