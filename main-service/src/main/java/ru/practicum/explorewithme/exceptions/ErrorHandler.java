package ru.practicum.explorewithme.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice("ru.practicum.explorewithme")
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleBadRequestException(final BadRequestException e) {
        return new ResponseEntity<>(new ApiError(
                e.getStackTrace(),
                HttpStatus.BAD_REQUEST,
                "For the requested operation the conditions are not met.",
                e.getMessage()
        ), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(new ApiError(
                HttpStatus.NOT_FOUND,
                "The required object was not found.",
                e.getMessage()
        ), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleConstraintViolationException(final ConstraintViolationException e) {
        return new ResponseEntity<>(new ApiError(
                HttpStatus.CONFLICT,
                "Integrity constraint has been violated",
                e.getMessage()
        ), HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleInternalServerError(final HttpServerErrorException.InternalServerError e) {
        return new ResponseEntity<>(new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error occurred",
                e.getMessage()
        ), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
