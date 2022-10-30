package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CompilationDto;
import ru.practicum.explorewithme.services.CompilationsService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class CompilationsController {

    private final CompilationsService compilationsService;

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@Positive @PathVariable Long compId) {
        log.info("Get compilation id={}", compId);
        return compilationsService.getCompilationPublic(compId);
    }

    @GetMapping
    public List<CompilationDto> findAllCompilations(@RequestParam(defaultValue = "true") Boolean pinned,
                                                    @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Find all compilations with parameters: pinned={}, from={}, size={}", pinned, from, size);
        return compilationsService.findAllCompilationsPublic(pinned, PageRequest.of(from / size, size));
    }
}
