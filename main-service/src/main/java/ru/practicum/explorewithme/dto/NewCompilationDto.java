package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    Set<Long> events;
    Boolean pinned;
    @NotBlank
    String title;
}
