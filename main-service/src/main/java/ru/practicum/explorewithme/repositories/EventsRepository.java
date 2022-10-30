package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.dto.EventShortDto;
import ru.practicum.explorewithme.models.Event;

import java.util.List;

public interface EventsRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByInitiatorId(Long userId, Pageable pageable);
}
