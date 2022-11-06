package ru.practicum.explorewitthme.stats;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatsOutDto {

    private Long id;
    private String app;
    private String uri;
    private Integer hits;
}
