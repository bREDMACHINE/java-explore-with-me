package ru.practicum.explorewithme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.Location;

public interface LocationsRepository extends JpaRepository<Location, Long> {
}
