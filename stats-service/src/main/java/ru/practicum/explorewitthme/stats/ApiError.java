package ru.practicum.explorewitthme.stats;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@ToString
public class ApiError {

    private String message;
    private String reason;
    private HttpStatus status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError(HttpStatus status, String reason, String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
        this.status = status;
    }

}
