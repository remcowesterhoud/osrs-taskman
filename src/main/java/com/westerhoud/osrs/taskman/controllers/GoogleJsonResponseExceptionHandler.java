package com.westerhoud.osrs.taskman.controllers;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.westerhoud.osrs.taskman.dto.sheet.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Slf4j
@ControllerAdvice
public class GoogleJsonResponseExceptionHandler {

    @Value("${sheets.api.serviceaccount.email}")
    private String serviceaccountEmail;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDto> handleException(final Exception e, final WebRequest request) {

        if (e instanceof GoogleJsonResponseException) {
            final GoogleJsonError error = ((GoogleJsonResponseException) e).getDetails();
            return switch (error.getCode()) {
                case 404 -> new ResponseEntity<>(ApiErrorDto.builder()
                        .code(error.getCode())
                        .message(String.format("Could not find spreadsheet with configured key. Please make sure you have set the" +
                                        " correct key in the configurations and that you have given editor access to %s.",
                                serviceaccountEmail))
                        .build(), HttpStatus.NOT_FOUND);
                case 429 -> {
                    log.error(e.getMessage(), e);
                    yield new ResponseEntity<>(ApiErrorDto.builder()
                            .code(error.getCode())
                            .message("Too many requests are being made. Please try again in a minute. If the problem persists please reach out.")
                            .build(), HttpStatus.TOO_MANY_REQUESTS);
                }
                default -> {
                    log.error(e.getMessage(), e);
                    yield new ResponseEntity<>(ApiErrorDto.builder()
                            .code(error.getCode())
                            .message("Something went wrong. Please try again later. If the problem persists please reach out.")
                            .build(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            };
        } else if (e instanceof ResponseStatusException) {
            return new ResponseEntity<>(ApiErrorDto.builder()
                    .code(((ResponseStatusException) e).getRawStatusCode())
                    .message(((ResponseStatusException) e).getReason())
                    .build(), ((ResponseStatusException) e).getStatus());
        } else {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(ApiErrorDto.builder()
                    .code(500)
                    .message("Something went wrong. Please try again later. If the problem persists please reach out.")
                    .build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
