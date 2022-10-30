package ru.practicum.explorewithme.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.services.EventsService;
import ru.practicum.explorewithme.dto.Sort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class EventsController {

    private final EventsService eventsService;

    @GetMapping("/{id}")
    public EventFullDto getEventPublic(@Positive @PathVariable Long id, HttpServletRequest request) {
        log.info("Get events id={}, client ip={}, endpoint path={}",
                id,
                request.getRemoteAddr(),
                request.getRequestURI());
        return eventsService.getEventPublic(id, request.getRemoteAddr(), request.getRequestURI());
    }

    @GetMapping
    public List<EventShortDto> findAllEventsPublic(@RequestParam String text,
                                                       @RequestParam List<Integer> categories,
                                                       @RequestParam Boolean paid,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @RequestParam LocalDateTime rangeStart,
                                                       @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                       @RequestParam LocalDateTime rangeEnd,
                                                       @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                       @RequestParam String sort,
                                                       @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                       @Positive @RequestParam(defaultValue = "10") Integer size,
                                                       HttpServletRequest request
                                                       ) {
        log.info("Find all events with parameters: text={}, " +
                "categories={}, " +
                "paid={}, " +
                "rangeStart={}, " +
                "rangeEnd={}, " +
                "onlyAvailable={}, " +
                "sort={}, " +
                "from={}, " +
                "size={}, " +
                "client ip={}, " +
                "endpoint path={}",
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                request.getRemoteAddr(),
                request.getRequestURI());
        Sort sortParam = Sort.from(sort).orElseThrow(() -> new NotFoundException("Category "));
        return eventsService.findAllEventsPublic(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                sortParam,
                onlyAvailable,
                PageRequest.of(from / size, size),
                request.getRemoteAddr(),
                request.getRequestURI());
    }
}
