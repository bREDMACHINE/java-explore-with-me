package ru.practicum.explorewitthme.stats;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
}
