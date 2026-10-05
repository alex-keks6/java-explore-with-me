package ru.practicum.explore.stats.server.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.stats.server.exception.ValidationException;
import ru.practicum.explore.stats.server.model.ErrorInfoResponse;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorInfoResponse handleValidation(final ValidationException exception) {
        return new ErrorInfoResponse(exception.getMessage());
    }
}