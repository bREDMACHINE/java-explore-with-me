package ru.practicum.explorewithme.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsOutDto {

    private Long id;
    private String app;
    private String uri;
    private Integer hits;
}
