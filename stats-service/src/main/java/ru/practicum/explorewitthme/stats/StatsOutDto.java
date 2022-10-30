package ru.practicum.explorewitthme.stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatsOutDto {

    private String app;
    private String uri;
    private Integer hits;
}
