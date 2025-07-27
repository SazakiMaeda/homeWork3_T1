package com.sazakimaeda.h2.handler;

import com.sazakimaeda.h2.handler.exception.QueueIsFullException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(QueueIsFullException.class)
    public ResponseStatusException handlerQueueIsFullException(Exception e) {
        return new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseStatusException handlerException(Exception e) {
        return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
