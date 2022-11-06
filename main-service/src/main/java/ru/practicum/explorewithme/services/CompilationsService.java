package ru.practicum.explorewithme.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;

import java.util.List;

public interface CompilationsService {
    CompilationDto getCompilationPublic(Long compId);

    List<CompilationDto> findAllCompilationsPublic(Boolean pinned, Pageable pageable);

    CompilationDto createCompilationsByAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationsByAdmin(Long compId);

    void deleteEventInCompilationsByAdmin(Long compId, Long eventId);

    CompilationDto addEventToCompilationsByAdmin(Long compId, Long eventId);

    void pinOnWallCompilationsByAdmin(Long compId);

    void deleteFromWallCompilationsByAdmin(Long compId);
}
