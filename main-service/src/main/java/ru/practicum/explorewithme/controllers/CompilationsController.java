package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.dto.NewCompilationDto;
import ru.practicum.explorewithme.services.CompilationsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class CompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilationPublic(@Positive @PathVariable Long compId) {
        log.info("Get /compilations/{}", compId);
        CompilationDto compilationDto = compilationsService.getCompilationPublic(compId);
        log.info("Return compilation={}", compilationDto);
        return compilationDto;
    }

    @GetMapping("/compilations")
    public List<CompilationDto> findAllCompilationsPublic(
            @RequestParam(defaultValue = "true") Boolean pinned,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        log.info("Get /compilations with parameters: pinned={}, from={}, size={}", pinned, from, size);
        List<CompilationDto> compilationDtos = compilationsService.findAllCompilationsPublic(
                pinned,
                PageRequest.of(from / size, size)
        );
        log.info("Return compilations={}", compilationDtos);
        return compilationDtos;
    }

    @PostMapping("/admin/compilations")
    public CompilationDto createCompilationsByAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post /admin/compilations with body compilation={}", newCompilationDto);
        CompilationDto compilationDto = compilationsService.createCompilationsByAdmin(newCompilationDto);
        log.info("Return compilation={}", compilationDto);
        return compilationDto;
    }

    @DeleteMapping("/admin/compilations/{compId}")
    public void deleteCompilationsByAdmin(@Positive @PathVariable Long compId) {
        log.info("Delete /admin/compilations/{}", compId);
        compilationsService.deleteCompilationsByAdmin(compId);
    }

    @DeleteMapping("/admin/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilationsByAdmin(@Positive @PathVariable Long compId,
                                                   @Positive @PathVariable Long eventId) {
        log.info("Delete /admin/compilations/{}/events/{}", compId, eventId);
        compilationsService.deleteEventInCompilationsByAdmin(compId, eventId);
    }

    @PatchMapping("/admin/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilationsByAdmin(@Positive @PathVariable Long compId,
                                                        @Positive @PathVariable Long eventId) {
        log.info("Patch /admin/compilations/{}/events/{}", compId, eventId);
        return compilationsService.addEventToCompilationsByAdmin(compId, eventId);
    }

    @DeleteMapping("/admin/compilations/{compId}/pin")
    public void deleteFromWallCompilationsByAdmin(@Positive @PathVariable Long compId) {
        log.info("Delete /admin/compilations/{}/pin", compId);
        compilationsService.deleteFromWallCompilationsByAdmin(compId);
    }

    @PatchMapping("/admin/compilations/{compId}/pin")
    public void pinOnWallCompilationsByAdmin(@Positive @PathVariable Long compId) {
        log.info("Patch /admin/compilations/{}/pin", compId);
        compilationsService.pinOnWallCompilationsByAdmin(compId);
    }
}
