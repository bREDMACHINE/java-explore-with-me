package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.CompilationMapper;
import ru.practicum.explorewithme.models.Compilation;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.repositories.CompilationsRepository;
import ru.practicum.explorewithme.services.CompilationsService;
import ru.practicum.explorewithme.services.EventsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CompilationsServiceImpl implements CompilationsService {

    private final CompilationsRepository compilationsRepository;
    private final EventsService eventsService;

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationPublic(Long compId) {
        Compilation compilation = getCompilationForServices(compId);
        return CompilationMapper.toCompilationDto(compilation,
                eventsService.findAllEventShortDtosForServices(compilation.getEvents()));
    }

    private Compilation getCompilationForServices(Long id) {
        return compilationsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + id + " was not found."));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> findAllCompilationsPublic(Boolean pinned, Pageable pageable) {
        return compilationsRepository.findAll(pageable).stream()
                .filter(compilation -> compilation.getPinned().equals(true))
                .map(compilation -> CompilationMapper.toCompilationDto(compilation,
                                eventsService.findAllEventShortDtosForServices(compilation.getEvents())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CompilationDto createCompilationsByAdmin(NewCompilationDto newCompilationDto) {
        List<Event> events = eventsService.findAllEventsForServices(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        return CompilationMapper.toCompilationDto(compilationsRepository.save(compilation),
                eventsService.findAllEventShortDtosForServices(compilation.getEvents()));
    }

    @Transactional
    @Override
    public void deleteCompilationsByAdmin(Long compId) {
        Compilation compilation = getCompilationForServices(compId);
        compilationsRepository.delete(compilation);
    }

    @Transactional
    @Override
    public void deleteEventInCompilationsByAdmin(Long compId, Long eventId) {
        Compilation compilation = getCompilationForServices(compId);
        compilation.getEvents().remove(eventsService.getEventForServices(eventId));
        compilationsRepository.save(compilation);
    }

    @Transactional
    @Override
    public CompilationDto addEventToCompilationsByAdmin(Long compId, Long eventId) {
        Compilation compilation = getCompilationForServices(compId);
        compilation.getEvents().add(eventsService.getEventForServices(eventId));
        return CompilationMapper.toCompilationDto(compilationsRepository.save(compilation),
                eventsService.findAllEventShortDtosForServices(compilation.getEvents()));
    }

    @Transactional
    @Override
    public void pinOnWallCompilationsByAdmin(Long compId) {
        Compilation compilation = getCompilationForServices(compId);
        compilation.setPinned(true);
        compilationsRepository.save(compilation);
    }

    @Transactional
    @Override
    public void deleteFromWallCompilationsByAdmin(Long compId) {
        Compilation compilation = getCompilationForServices(compId);
        compilation.setPinned(false);
        compilationsRepository.save(compilation);
    }
}
