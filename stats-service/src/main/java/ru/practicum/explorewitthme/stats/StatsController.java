package ru.practicum.explorewitthme.stats;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class StatsController {

    private final StatsService statsService;

    @GetMapping("/stats")
    public List<StatsOutDto> getStats(@RequestParam
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                LocalDateTime start,
                                @RequestParam
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                LocalDateTime end,
                                @RequestParam List<String> uris,
                                @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get stats with parameters:  start={}, end={}, uris={}, unique={}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping ("/hit")
    public void setViews(@RequestBody StatsDto statsDto) {
        log.info("Set stats={}", statsDto);
        statsService.setViews(statsDto);
    }
}
