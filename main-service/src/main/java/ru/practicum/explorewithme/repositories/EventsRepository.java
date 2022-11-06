package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.Event;

import java.util.List;
import java.util.Set;

public interface EventsRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);

    List<Event> findEventsByIdIn(Set<Long> ids);
}
