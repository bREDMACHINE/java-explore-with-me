package ru.practicum.explorewitthme.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StatsDto {

    private Long id;
    private String app;
    private String uri;
    private String ip;
    private LocalDateTime timestamp;
}
