package org.example.exception;

public class InvalidRequestException extends RuntimeException {

    private String message;

    public InvalidRequestException(final String message) {
        super(message);
    }

}
