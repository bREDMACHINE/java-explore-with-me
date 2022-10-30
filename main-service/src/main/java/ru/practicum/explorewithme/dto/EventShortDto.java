package ru.practicum.explorewithme.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {

    private Long id;
    private String annotation;
    private EventShortDto.CategoryDto category;
    private Long confirmedRequests;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private EventShortDto.UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CategoryDto {

        private Long id;
        private String name;
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserShortDto {

        private Long id;
        private String name;
    }
}
