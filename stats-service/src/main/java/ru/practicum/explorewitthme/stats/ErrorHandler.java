package ru.practicum.explorewitthme.stats;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice("ru.practicum.explorewithme.stats")
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity<ApiError> handleNotFoundException(final NotFoundException e) {
        return new ResponseEntity<>(new ApiError(
                HttpStatus.NOT_FOUND,
                "The required object was not found.",
                e.getMessage()
        ), HttpStatus.NOT_FOUND);
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
