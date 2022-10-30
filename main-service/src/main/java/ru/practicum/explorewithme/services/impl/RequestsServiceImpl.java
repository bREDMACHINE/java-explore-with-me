package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.Status;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.EventMapper;
import ru.practicum.explorewithme.mappers.RequestMapper;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.Request;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.repositories.RequestsRepository;
import ru.practicum.explorewithme.services.EventsService;
import ru.practicum.explorewithme.services.RequestsService;
import ru.practicum.explorewithme.services.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RequestsServiceImpl implements RequestsService {

    private final RequestsRepository requestsRepository;
    private final UserService userService;
    private final EventsService eventsService;

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findAllRequestsByInitiator(Long userId, Long eventId) {
        userService.getUserForServices(userId);
        return requestsRepository.findRequestsByEventId(eventsService.getEventForServices(eventId).getId()).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList()
                );
    }

    @Transactional
    @Override
    public ParticipationRequestDto confirmRequestByInitiator(Long userId, Long eventId, Long reqId) {
        User user = userService.getUserForServices(userId);
        Event event = eventsService.getEventForServices(eventId);
        Long requests = 0L;
        if (event.getConfirmedRequests() != null) {
            requests = event.getConfirmedRequests();
        }
        if (Objects.equals(user.getId(), event.getInitiator().getId()) && requests < event.getParticipantLimit()) {
            Request request = getRequestForServices(reqId);
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(++requests);
            eventsService.updateEventByUser(userId, EventMapper.toUpdateEvent(event));
            return RequestMapper.toRequestDto(requestsRepository.save(request));
        }
        throw new BadRequestException("Only initiator can accept requests");
    }

    private Request getRequestForServices(Long reqId) {
        return requestsRepository.findById(reqId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + reqId + " was not found."));
    }

    @Transactional
    @Override
    public ParticipationRequestDto rejectRequestByInitiator(Long userId, Long eventId, Long reqId) {
        Event event = eventsService.getEventForServices(eventId);
        if (Objects.equals(userService.getUserForServices(userId).getId(), event.getInitiator().getId())) {
            Request request = getRequestForServices(reqId);
            request.setStatus(Status.REJECTED);
            return RequestMapper.toRequestDto(requestsRepository.save(request));
        }
        throw new BadRequestException("Only initiator can reject requests");
    }

    @Transactional(readOnly = true)
    @Override
    public List<ParticipationRequestDto> findAllRequestsByRequester(Long userId) {
        return requestsRepository.findByRequesterId(userId).stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequestByRequester(Long userId, Long eventId) {
        User user = userService.getUserForServices(userId);
        Event event = eventsService.getEventForServices(eventId);
        Request request = RequestMapper.toRequest(user, event);
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            Long requests = 0L;
            if (event.getConfirmedRequests() != null) {
                requests = event.getConfirmedRequests();
            }
            request.setStatus(Status.CONFIRMED);
            event.setConfirmedRequests(++requests);
            eventsService.updateEventByUser(userId, EventMapper.toUpdateEvent(event));
        }
        return RequestMapper.toRequestDto(requestsRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestByRequester(Long userId, Long requestId) {
        User user = userService.getUserForServices(userId);
        Request request = getRequestForServices(requestId);
        if (Objects.equals(user.getId(), request.getRequester().getId())) {
            request.setStatus(Status.CANCELED);
            return RequestMapper.toRequestDto(requestsRepository.save(request));
        }
        throw new BadRequestException("Only requester can cancel requests");
    }
}
