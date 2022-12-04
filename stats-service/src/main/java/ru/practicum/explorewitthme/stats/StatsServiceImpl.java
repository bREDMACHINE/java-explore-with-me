package ru.practicum.explorewitthme.stats;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsRepositoryCustom statsRepositoryCustom;

    @Transactional(readOnly = true)
    public List<StatsOutDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        String[] urisArray = uris.toArray(new String[uris.size()]);
        if (urisArray.length == 1) {
            urisArray[0] = urisArray[0].substring(1, urisArray[0].length() - 1);
        }
        if (urisArray.length > 1) {
            urisArray[0] = urisArray[0].substring(1);
            urisArray[urisArray.length - 1] = urisArray[urisArray.length - 1].substring(0, urisArray[urisArray.length - 1].length() - 1);
        }
        List<String> urisList = Arrays.asList(urisArray);
        List<Stats> stats = statsRepositoryCustom.getStats(start, end, urisList);
        List<StatsOutDto> statsOutDtos = new ArrayList<>();
        for (String uri : urisList) {
            List<Stats> list = new ArrayList<>();
            for (Stats stat : stats) {
                if (stat.getUri().equals(uri)) {
                    list.add(stat);
                }
            }
            statsOutDtos.add(StatsMapper.toStatsOutDto(list, uri, unique));
        }
        log.info("Return stats={}", statsOutDtos);
        return statsOutDtos;
    }

    @Transactional
    public void setViews(StatsDto statsDto) {
        Stats stats = StatsMapper.toStats(statsDto);
        log.info("statsRepository.save({})", stats);
        statsRepository.save(stats);
    }
}