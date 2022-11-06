package ru.practicum.explorewithme.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.services.EventsService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class EventsController {

    private final EventsService eventsService;

    @GetMapping("/events/{id}")
    public EventFullDto getEventPublic(@Positive @PathVariable Long id, HttpServletRequest request) {
        log.info("Get /events/{}, client ip={}, endpoint path={}",
                id,
                request.getRemoteAddr(),
                request.getRequestURI());
        EventFullDto eventFullDto = eventsService.getEventPublic(id, request.getRemoteAddr(), request.getRequestURI());
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/events")
    public List<EventShortDto> findAllEventsPublic(
            @RequestParam String text,
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
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size,
            HttpServletRequest request
    ) {
        log.info("Get /events with parameters: text={}, " +
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
        Sort sortParam = Sort.from(sort).orElseThrow(() -> new BadRequestException("Bad sort parameters"));
        List<EventShortDto> eventShortDtos = eventsService.findAllEventsPublic(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                sortParam,
                onlyAvailable,
                PageRequest.of(from / size, size),
                request.getRemoteAddr(),
                request.getRequestURI());
        log.info("Return event={}", eventShortDtos);
        return eventShortDtos;
    }

    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> findAllEventsByUser(
            @Positive @PathVariable Long userId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        log.info("Get /users/{}/events with parameters from={}, size={}", userId, from, size);
        List<EventShortDto> eventShortDtos = eventsService.findAllEventsByUser(userId, PageRequest.of(from / size, size));
        log.info("Return event={}", eventShortDtos);
        return eventShortDtos;
    }

    @PatchMapping("/users/{userId}/events")
    public EventFullDto updateEventByUser(@Positive@PathVariable Long userId,
                                          @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Patch /users/{}/events with body event={}", userId, updateEventRequest);
        EventFullDto eventFullDto =  eventsService.updateEventByUser(userId, updateEventRequest);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @PostMapping("/users/{userId}/events")
    public EventFullDto createEventByUser(@Positive @PathVariable Long userId,
                                          @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Post /users/{}/events with body event={}", userId, newEventDto);
        EventFullDto eventFullDto =  eventsService.createEventByUser(userId, newEventDto);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.info("Get /users/{}/events/{}", userId, eventId);
        EventFullDto eventFullDto =  eventsService.getEventByUser(userId, eventId);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto cancelEventByUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.info("Patch /users/{}/events/{}", userId, eventId);
        EventFullDto eventFullDto = eventsService.cancelEventByUser(userId, eventId);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> findAllEventsByAdmin(
            @RequestParam List<Integer> users,
            @RequestParam List<String> states,
            @RequestParam List<Integer> categories,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam LocalDateTime rangeStart,
            @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
            @RequestParam LocalDateTime rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        log.info("Get /admin/events with parameters: users={}, " +
                "states={}, " +
                "categories={}, " +
                "rangeStart={}, " +
                "rangeEnd={}, " +
                "from={}, " +
                "size={},", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> eventFullDtos = eventsService.findAllEventsByAdmin(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                PageRequest.of(from / size, size));
        log.info("Return events={}", eventFullDtos);
        return eventFullDtos;
    }

    @PutMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@Positive @PathVariable Long eventId,
                                           @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Put /admin/events/{} with body event={},", eventId, adminUpdateEventRequest);
        EventFullDto eventFullDto = eventsService.updateEventByAdmin(eventId, adminUpdateEventRequest);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/admin/events/{eventId}/publish")
    public EventFullDto publishEventByAdmin(@Positive @PathVariable Long eventId) {
        log.info("Patch /admin/events/{}/publish", eventId);
        EventFullDto eventFullDto = eventsService.publishEventByAdmin(eventId);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }

    @PatchMapping("/admin/events/{eventId}/reject")
    public EventFullDto rejectEventByAdmin(@Positive @PathVariable Long eventId) {
        log.info("Patch /admin/events/{}/reject", eventId);
        EventFullDto eventFullDto = eventsService.rejectEventByAdmin(eventId);
        log.info("Return event={}", eventFullDto);
        return eventFullDto;
    }
}
