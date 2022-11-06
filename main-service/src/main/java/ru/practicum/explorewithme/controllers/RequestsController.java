package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.services.RequestsService;

import javax.validation.constraints.Positive;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class RequestsController {

    private final RequestsService requestsService;

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> findAllRequestsByInitiator(@Positive @PathVariable Long userId,
                                                                    @Positive @PathVariable Long eventId) {
        log.info("Get /users/{}/events/{}/requests", userId, eventId);
        List<ParticipationRequestDto> requestDtos = requestsService.findAllRequestsByInitiator(userId, eventId);
        log.info("Return requests={}", requestDtos);
        return requestDtos;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/confirm")
    public ParticipationRequestDto confirmRequestByInitiator(@Positive @PathVariable Long userId,
                                                             @Positive @PathVariable Long eventId,
                                                             @Positive @PathVariable Long reqId) {
        log.info("Patch /users/{}/events/{}/requests/{}/confirm", userId, eventId, reqId);
        ParticipationRequestDto requestDto = requestsService.confirmRequestByInitiator(userId, eventId, reqId);
        log.info("Return request={}", requestDto);
        return requestDto;
    }

    @PatchMapping("/{userId}/events/{eventId}/requests/{reqId}/reject")
    public ParticipationRequestDto rejectRequestByInitiator(@Positive @PathVariable Long userId,
                                                            @Positive @PathVariable Long eventId,
                                                            @Positive @PathVariable Long reqId) {
        log.info("Patch /users/{}/events/{}/requests/{}/reject", userId, eventId, reqId);
        ParticipationRequestDto requestDto = requestsService.rejectRequestByInitiator(userId, eventId, reqId);
        log.info("Return request={}", requestDto);
        return requestDto;
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> findAllRequestsByRequester(@Positive @PathVariable Long userId) {
        log.info("Get /users/{}/requests", userId);
        List<ParticipationRequestDto> requestDtos = requestsService.findAllRequestsByRequester(userId);
        log.info("Return requests={}", requestDtos);
        return requestDtos;
    }

    @PostMapping("/{userId}/requests")
    public ParticipationRequestDto createRequestByRequester(@Positive @PathVariable Long userId,
                                                            @Positive @RequestParam Long eventId) {
        log.info("Post /users/{}/requests with parameter eventId={}", userId, eventId);
        ParticipationRequestDto requestDto = requestsService.createRequestByRequester(userId, eventId);
        log.info("Return request={}", requestDto);
        return requestDto;
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByRequester(@Positive @PathVariable Long userId,
                                                            @Positive @PathVariable Long requestId) {
        log.info("Patch /users/{}/requests/{}/cancel", userId, requestId);
        ParticipationRequestDto requestDto = requestsService.cancelRequestByRequester(userId, requestId);
        log.info("Return request={}", requestDto);
        return requestDto;
    }
}
