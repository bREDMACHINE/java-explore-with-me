package ru.practicum.explorewithme.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.models.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventsService {

    List<EventShortDto> findAllEventsPublic(String text,
                                      List<Integer> categories,
                                      Boolean paid,
                                      LocalDateTime rangeStart,
                                      LocalDateTime rangeEnd,
                                      Sort sort,
                                      Boolean onlyAvailable,
                                      Pageable pageable,
                                      String ip,
                                      String uri);

    List<Event> findAllEventsForServices(Set<Long> ids);

    EventFullDto getEventPublic(Long id, String ip, String uri);

    Event getEventForServices(Long id);

    List<EventShortDto> findAllEventsByUser(Long userId, Pageable pageable);

    EventFullDto updateEventByUser(Long userId, UpdateEventRequest updateEventRequest);

    EventFullDto createEventByUser(Long userId, NewEventDto newEventDto);

    EventFullDto cancelEventByUser(Long userId, Long eventId);

    EventFullDto getEventByUser(Long userId, Long eventId);

    EventFullDto rejectEventByAdmin(Long eventId);

    EventFullDto publishEventByAdmin(Long eventId);

    EventFullDto updateEventByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest);

    List<EventFullDto> findAllEventsByAdmin(List<Integer> users,
                                            List<String> states,
                                            List<Integer> categories,
                                            LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd,
                                            Pageable pageable);

    List<EventShortDto> findAllEventShortDtosForServices(List<Event> events);

    List<Event> findAllEventsWithStatsForServices(Set<Event> events);
}
