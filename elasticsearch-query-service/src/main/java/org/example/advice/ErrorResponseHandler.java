package org.example.advice;

import lombok.extern.slf4j.Slf4j;
import org.example.exception.ElasticSearchExecutionException;
import org.example.exception.InvalidRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ErrorResponseHandler {

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(
        final InvalidRequestException e) {
        log.error("InvalidRequestException occurred ", e);
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(
            new ErrorResponse(e.getMessage(), badRequest.getReasonPhrase(), badRequest.value()),
            badRequest);
    }

    @ExceptionHandler(ElasticSearchExecutionException.class)
    public ResponseEntity<ErrorResponse> handleElasticsearchExecutionException(
        final ElasticSearchExecutionException e) {
        log.error("ElasticSearchExecutionException occurred ", e);
        final HttpStatus internalServer = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
            new ErrorResponse(e.getMessage(), internalServer.getReasonPhrase(),
                internalServer.value()), internalServer);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception e) {
        log.error("Exception occurred ", e);
        final HttpStatus internalServer = HttpStatus.INTERNAL_SERVER_ERROR;
        return new ResponseEntity<>(
            new ErrorResponse("Internal Server Error", internalServer.getReasonPhrase(),
                internalServer.value()), internalServer);
    }
}


