package ru.practicum.explorewithme.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    private Set<Long> events;
    private Boolean pinned;
    @NotBlank
    private String title;
}
