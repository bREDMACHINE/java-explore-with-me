package ru.practicum.explorewithme.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@ToString
public class ApiError {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<StackTraceElement> errors;
    private String message;
    private String reason;
    private HttpStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError(StackTraceElement[] stackTrace, HttpStatus status, String reason, String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
        this.status = status;
        this.errors = List.of(stackTrace);
    }

    public ApiError(HttpStatus status, String reason, String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
        this.status = status;
    }

}
