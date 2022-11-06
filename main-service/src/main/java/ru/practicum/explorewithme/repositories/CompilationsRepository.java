package ru.practicum.explorewithme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.Compilation;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
}
