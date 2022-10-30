package ru.practicum.explorewitthme.stats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepositoryCustom {

    List<Stats> getStats(LocalDateTime start, LocalDateTime end, String uri, Boolean unique);
}
