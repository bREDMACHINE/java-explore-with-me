package ru.practicum.explorewitthme.stats;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;
    private final StatsRepositoryCustom statsRepositoryCustom;

    @Transactional(readOnly = true)
    public List<StatsOutDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsOutDto> statsOutDtos = new ArrayList<>();
        for (String uri : uris) {
            List<Stats> stats = statsRepositoryCustom.getStats(start, end, uri, unique);
            statsOutDtos.add(StatsMapper.toStatsOutDto(stats, uri));
        }
        return statsOutDtos;
    }

    @Transactional
    public void setViews(StatsDto statsDto) {
        statsRepository.save(StatsMapper.toStats(statsDto));
    }
}