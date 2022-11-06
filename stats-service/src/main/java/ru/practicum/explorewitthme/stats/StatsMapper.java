package ru.practicum.explorewitthme.stats;

import java.util.List;

public class StatsMapper {

    public static Stats toStats(StatsDto statsDto) {
       Stats stats = new Stats();
       stats.setApp(statsDto.getApp());
       stats.setIp(statsDto.getIp());
       stats.setUri(statsDto.getUri());
       stats.setDateTimeRequest(statsDto.getTimestamp());
       return stats;
    }

    public static StatsOutDto toStatsOutDto(List<Stats> stats, String uri) {
        StatsOutDto statsOutDto = new StatsOutDto();
        String[] uriString = uri.split("/");
        String idString = uriString[2].substring(0, uriString[2].length() - 1);
        statsOutDto.setId(Long.parseLong(idString));
        if (stats.size() != 0) {
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
