package ru.practicum.explorewitthme.stats;

import java.util.List;
import java.util.stream.Collectors;

public class StatsMapper {

    public static Stats toStats(StatsDto statsDto) {
       Stats stats = new Stats();
       stats.setApp(statsDto.getApp());
       stats.setIp(statsDto.getIp());
       stats.setUri(statsDto.getUri());
       stats.setDateTimeRequest(statsDto.getTimestamp());
       return stats;
    }

    public static StatsOutDto toStatsOutDto(List<Stats> stats, String uri, Boolean unique) {
        StatsOutDto statsOutDto = new StatsOutDto();
        if (stats.size() != 0) {
            if (unique) {
                List<String> statsUnique = stats.stream()
                        .map(Stats::getIp)
                        .distinct()
                        .collect(Collectors.toList());
                statsOutDto.setApp(stats.get(0).getApp());
                statsOutDto.setUri(stats.get(0).getUri());
                statsOutDto.setHits(statsUnique.size());
                return statsOutDto;
            }
            statsOutDto.setApp(stats.get(0).getApp());
            statsOutDto.setUri(stats.get(0).getUri());
            statsOutDto.setHits(stats.size());
            return statsOutDto;
        }
        statsOutDto.setApp("ewm-main-service");
        statsOutDto.setUri(uri);
        statsOutDto.setHits(0);
        return statsOutDto;
    }
}
