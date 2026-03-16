package com.mdotm.petapi.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.List;

/**
 * Standard error envelope returned by the global exception handler.
 *
 * @param timestamp   when the error occurred (ISO-8601 UTC)
 * @param status      HTTP status code
 * @param error       short error label (e.g. "Not Found")
 * @param message     human-readable description
 * @param fieldErrors per-field validation errors; omitted from JSON when null
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        List<FieldError> fieldErrors
) {

    public static ErrorResponse of(int status, String error, String message) {
        return new ErrorResponse(Instant.now(), status, error, message, null);
    }

    public static ErrorResponse of(int status, String error, String message, List<FieldError> fieldErrors) {
        return new ErrorResponse(Instant.now(), status, error, message, fieldErrors);
    }

    public record FieldError(String field, String message) {}
}
