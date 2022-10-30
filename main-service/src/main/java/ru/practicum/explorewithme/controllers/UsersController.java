package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.EventFullDto;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.dto.NewEventDto;
import ru.practicum.explorewithme.dto.UpdateEventRequest;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.services.EventsService;
import ru.practicum.explorewithme.services.RequestsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class UsersController {

    private final EventsService eventsService;
    private final RequestsService requestsService;


    @GetMapping("/{userId}/events")
    public List<EventShortDto> findAllEventsByUser(@Positive @PathVariable Long userId,
                                                   @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get events id={}, from={}, size={}", userId, from, size);
        return eventsService.findAllEventsByUser(userId, PageRequest.of(from / size, size));
    }

    @PatchMapping("/{userId}/events")
    public EventFullDto updateEventByUser(@Positive@PathVariable Long userId,
                                          @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        log.info("Patch event id={}, event={}", userId, updateEventRequest);
        return eventsService.updateEventByUser(userId, updateEventRequest);
    }

    @PostMapping("/{userId}/events")
    public EventFullDto createEventByUser(@Positive @PathVariable Long userId,
                                          @Valid @RequestBody NewEventDto newEventDto) {
        log.info("Post event id={}, event={}", userId, newEventDto);
        return eventsService.createEventByUser(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.info("Get event userId={}, eventId={}", userId, eventId);
        return eventsService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto cancelEventByUser(@Positive @PathVariable Long userId, @Positive @PathVariable Long eventId) {
        log.info("Patch event userId={}, eventId={}", userId, eventId);
        return eventsService.cancelEventByUser(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findAllRequestsByInitiator(@Positive @PathVariable Long userId,
                                                                    @Positive @PathVariable Long eventId) {
        log.info("Get all requests by event userId={}, eventId={}", userId, eventId);
        return requestsService.findAllRequestsByInitiator(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestByInitiator(@Positive @PathVariable Long userId,
                                                             @Positive @PathVariable Long eventId,
                                                             @Positive @PathVariable Long reqId) {
        log.info("Patch confirm request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return requestsService.confirmRequestByInitiator(userId, eventId, reqId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestByInitiator(@Positive @PathVariable Long userId,
                                                            @Positive @PathVariable Long eventId,
                                                            @Positive @PathVariable Long reqId) {
        log.info("Patch reject request userId={}, eventId={}, reqId={}", userId, eventId, reqId);
        return requestsService.rejectRequestByInitiator(userId, eventId, reqId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findAllRequestsByRequester(@Positive @PathVariable Long userId) {
        log.info("Get all requests by userId={}", userId);
        return requestsService.findAllRequestsByRequester(userId);
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequestByRequester(@Positive @PathVariable Long userId,
                                                            @Positive @RequestParam Long eventId) {
        log.info("Post request userId={}, eventId={}", userId, eventId);
        return requestsService.createRequestByRequester(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByRequester(@Positive @PathVariable Long userId,
                                                            @Positive @PathVariable Long requestId) {
        log.info("Patch cancel request userId={}, requestId={}", userId, requestId);
        return requestsService.cancelRequestByRequester(userId, requestId);
    }
}
