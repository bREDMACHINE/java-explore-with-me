package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.client.StatsClientImpl;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.EventMapper;
import ru.practicum.explorewithme.mappers.LocationMapper;
import ru.practicum.explorewithme.models.Category;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.Location;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.repositories.EventsRepository;
import ru.practicum.explorewithme.repositories.EventsRepositoryCustom;
import ru.practicum.explorewithme.repositories.LocationsRepository;
import ru.practicum.explorewithme.services.CategoriesService;
import ru.practicum.explorewithme.services.EventsService;
import ru.practicum.explorewithme.services.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EventsServiceImpl implements EventsService {

    private final EventsRepository eventsRepository;
    private final EventsRepositoryCustom eventsRepositoryCustom;
    private final UserService userService;
    private final CategoriesService categoriesService;
    private final LocationsRepository locationsRepository;
    private final StatsClientImpl statsClient;

    private void setViews(String ip, String uri) {
        StatsDto statsDto = new StatsDto();
        statsDto.setApp("ewm-main-service");
        statsDto.setIp(ip);
        statsDto.setUri(uri);
        statsDto.setTimestamp(LocalDateTime.now());
        statsClient.setViews(statsDto);
    }

    private List<Event> getStats(List<Event> events, Boolean unique) {
        if (events.size() != 0) {
            List<String> uris = new ArrayList<>();
            LocalDateTime start = LocalDateTime.now();
            for (Event event : events) {
                uris.add("/events/" + event.getId());
                if (event.getCreatedOn().minusDays(1).isBefore(start)) {
                    start = event.getCreatedOn().minusDays(1);
                }
            }
            List<StatsOutDto> statsDtos = statsClient.getStats(uris, start, LocalDateTime.now(), unique).getBody();
            if (statsDtos != null) {
                for (Event event : events) {
                    for (StatsOutDto stats : statsDtos) {
                        String[] uriString = stats.getUri().split("/");
                        String idString = uriString[2];
                        if (Objects.equals(event.getId(), Long.parseLong(idString))) {
                            event.setViews(stats.getHits());
                        }
                    }
                }
            }
        }
        return events;
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findAllEventsPublic(
            String text,
            List<Integer> categories,
            Boolean paid,
            LocalDateTime rangeStart,
            LocalDateTime rangeEnd,
            Sort sort,
            Boolean onlyAvailable,
            Pageable pageable,
            String ip,
            String uri
    ) {
        setViews(ip, uri);
        if (sort.equals(Sort.VIEWS)) {
            return getStats(
                    eventsRepositoryCustom.findAllEventsPublic(text, categories, rangeStart, rangeEnd, sort, onlyAvailable, pageable),
                    false
            ).stream()
                    .filter(event -> paid != null ? event.getPaid().equals(paid) : true)
                    .map(EventMapper::toEventShortDto)
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }
        return getStats(
                eventsRepositoryCustom.findAllEventsPublic(text, categories, rangeStart, rangeEnd, sort, onlyAvailable, pageable),
                false
        ).stream()
                .filter(event -> paid != null ? event.getPaid().equals(paid) : true)
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findAllEventsForServices(Set<Long> ids) {
        return getStats(eventsRepository.findEventsByIdIn(ids), false);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findAllEventShortDtosForServices(List<Event> events) {
        return getStats(events, false).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<Event> findAllEventsWithStatsForServices(Set<Event> events) {
        return getStats(new ArrayList<>(events), false);
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventPublic(Long id, String ip, String uri) {
        Event event = getEventForServices(id);
        if (event.getState().equals(State.PUBLISHED)) {
            setViews(ip, uri);
            return EventMapper.toEventFullDto(event);
        }
        throw new NotFoundException("Event with id=" + id + " was not found.");
    }

    @Transactional(readOnly = true)
    @Override
    public Event getEventForServices(Long id) {
        Event event = eventsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Event with id=" + id + " was not found."));
        return getStats(List.of(event), true).get(0);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventShortDto> findAllEventsByUser(Long userId, Pageable pageable) {
        return getStats(
                eventsRepository.findAllByInitiatorId(userService.getUserForServices(userId).getId(), pageable),
                false
        ).stream()
                .map(EventMapper::toEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(Long userId, UpdateEventRequest updateEventRequest) {
        userService.getUserForServices(userId);
        Event event = getEventForServices(updateEventRequest.getEventId());
        if (event.getState().equals(State.PENDING) || event.getState().equals(State.CANCELED)) {
            if (event.getState().equals(State.CANCELED)) {
                event.setState(State.PENDING);
            }
            if (updateEventRequest.getAnnotation() != null) {
                event.setAnnotation(updateEventRequest.getAnnotation());
            }
            if (updateEventRequest.getCategory() != null) {
                event.setCategory(categoriesService.getCategoryForServices(updateEventRequest.getCategory()));
            }
            if (updateEventRequest.getDescription() != null) {
                event.setDescription(updateEventRequest.getDescription());
            }
            if (updateEventRequest.getEventDate() != null) {
                event.setEventDate(updateEventRequest.getEventDate());
            }
            if (updateEventRequest.getPaid() != null) {
                event.setPaid(updateEventRequest.getPaid());
            }
            if (updateEventRequest.getParticipantLimit() != null) {
                event.setParticipantLimit(updateEventRequest.getParticipantLimit());
            }
            if (updateEventRequest.getTitle() != null) {
                event.setTitle(updateEventRequest.getTitle());
            }
            return EventMapper.toEventFullDto(eventsRepository.save(event));
        }
        if (updateEventRequest.getConfirmedRequests() != null) {
            event.setConfirmedRequests(updateEventRequest.getConfirmedRequests());
            return EventMapper.toEventFullDto(eventsRepository.save(event));
        }
        throw new BadRequestException("The change is not available");
    }

    @Transactional
    @Override
    public EventFullDto createEventByUser(Long userId, NewEventDto newEventDto) {
        User user = userService.getUserForServices(userId);
        Category category = categoriesService.getCategoryForServices(newEventDto.getCategory());
        Location location = LocationMapper.toLocation(newEventDto.getLocation());
        Event event = EventMapper.toEvent(newEventDto, category, locationsRepository.save(location), user);
        return EventMapper.toEventFullDto(getStats(List.of(eventsRepository.save(event)), false).get(0));
    }

    @Transactional
    @Override
    public EventFullDto cancelEventByUser(Long userId, Long eventId) {
        userService.getUserForServices(userId);
        Event event = getEventForServices(eventId);
        if (event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
            return EventMapper.toEventFullDto(eventsRepository.save(event));
        }
        throw new BadRequestException("The change is not available");
    }

    @Transactional(readOnly = true)
    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        User user = userService.getUserForServices(userId);
        Event event = getEventForServices(eventId);
        if (event.getInitiator().getId().equals(user.getId())) {
            return EventMapper.toEventFullDto(event);
        }
        throw new NotFoundException("Event with id=" + eventId + " was not found.");
    }

    @Transactional
    @Override
    public EventFullDto rejectEventByAdmin(Long eventId) {
        Event event = getEventForServices(eventId);
        if (event.getState().equals(State.PENDING)) {
            event.setState(State.CANCELED);
            return EventMapper.toEventFullDto(eventsRepository.save(event));
        }
        throw new BadRequestException("The change is not available");
    }

    @Transactional
    @Override
    public EventFullDto publishEventByAdmin(Long eventId) {
        Event event = getEventForServices(eventId);
        LocalDateTime publishedOn = LocalDateTime.now();
        if (event.getState().equals(State.PENDING) && publishedOn.plusHours(1).isBefore(event.getEventDate())) {
            event.setState(State.PUBLISHED);
            event.setPublishedOn(publishedOn);
            return EventMapper.toEventFullDto(eventsRepository.save(event));
        }
        throw new BadRequestException("The change is not available");
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        Event event = getEventForServices(eventId);
        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            event.setCategory(categoriesService.getCategoryForServices(adminUpdateEventRequest.getCategory()));
        }
        if (adminUpdateEventRequest.getConfirmedRequests() != null) {
            event.setConfirmedRequests(adminUpdateEventRequest.getConfirmedRequests());
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            Location location = LocationMapper.toLocation(adminUpdateEventRequest.getLocation());
            event.setLocation(location);
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        return EventMapper.toEventFullDto(eventsRepository.save(event));
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> findAllEventsByAdmin(List<Integer> users,
                                                   List<String> statesString,
                                                   List<Integer> categories,
                                                   LocalDateTime rangeStart,
                                                   LocalDateTime rangeEnd,
                                                   Pageable pageable) {
        List<State> states = new ArrayList<>();
        if (statesString != null) {
            for (String state : statesString) {
                if (State.from(state).isPresent()) {
                    states.add(State.from(state).get());
                }
            }
        }
        return getStats(
                eventsRepositoryCustom.findAllEventsByAdmin(users, states, categories, rangeStart, rangeEnd, pageable),
                false
        ).stream()
                .map(EventMapper::toEventFullDto)
                .collect(Collectors.toList());
    }
}
