package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    List<EventShortDto> events;
    Long id;
    Boolean pinned;
    String title;
}
