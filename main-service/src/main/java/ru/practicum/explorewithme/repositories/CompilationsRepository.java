package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.models.Compilation;

import java.util.List;

public interface CompilationsRepository extends JpaRepository<Compilation, Long> {
    List<Compilation> findByPinnedTrue(Pageable pageable);
    List<Compilation> findByPinnedFalse(Pageable pageable);
}
