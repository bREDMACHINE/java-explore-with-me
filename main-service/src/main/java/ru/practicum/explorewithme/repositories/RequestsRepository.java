package ru.practicum.explorewithme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.Request;

import java.util.List;

public interface RequestsRepository extends JpaRepository<Request, Long> {

    List<Request> findByRequesterId(Long userId);

    List<Request> findRequestsByEventId(Long eventId);
}
