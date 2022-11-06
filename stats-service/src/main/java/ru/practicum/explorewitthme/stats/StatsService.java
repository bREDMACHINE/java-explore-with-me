package ru.practicum.explorewitthme.stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    List<StatsOutDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void setViews(StatsDto statsDto);
}
