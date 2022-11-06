package ru.practicum.explorewithme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    private String description;
    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private NewEventDto.LocationDto location;
    @Value("${NewEventDto.paid:false}")
    private Boolean paid;
    @Value("${NewEventDto.participantLimit:10}")
    private Long participantLimit;
    @Value("${NewEventDto.requestModeration:true}")
    private Boolean requestModeration;
    @NotBlank
    private String title;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDto {

        private Float lat;
        private Float lon;
    }
}
