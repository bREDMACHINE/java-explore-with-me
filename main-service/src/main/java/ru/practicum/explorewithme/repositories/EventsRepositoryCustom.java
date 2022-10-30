package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.Sort;
import ru.practicum.explorewithme.dto.State;
import ru.practicum.explorewithme.models.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventsRepositoryCustom {

    List<Event> findAllEventsByAdmin(List<Integer> users,
                                     List<State> states,
                                     List<Integer> categories,
                                     LocalDateTime rangeStart,
                                     LocalDateTime rangeEnd,
                                     Pageable pageable);

    List<Event> findAllEventsPublic(String text,
                                    List<Integer> categories,
                                    LocalDateTime rangeStart,
                                    LocalDateTime rangeEnd,
                                    Sort sort,
                                    Boolean onlyAvailable,
                                    Pageable pageable);
}
