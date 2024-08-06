package org.example.exception;

public class ElasticSearchExecutionException extends RuntimeException {

    private String message;

    public ElasticSearchExecutionException(final String message) {
        super(message);
    }

}
