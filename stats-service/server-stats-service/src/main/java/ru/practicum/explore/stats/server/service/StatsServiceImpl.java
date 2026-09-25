package ru.practicum.explore.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.dto.StatsDto;
import ru.practicum.explore.stats.server.model.Hit;
import ru.practicum.explore.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatsDto> statsDtoList = new ArrayList<>();
        Map<List<String>, List<String>> statsData = new HashMap<>();
        List<Hit> hitList = statsRepository.findAllByTimestampBetweenAndUriIn(start, end, uris);

        for (Hit hit : hitList) {
            List<String> statsKey = List.of(hit.getApp(), hit.getUri());
            if (statsData.containsKey(statsKey)) {
                if (!unique || !statsData.get(statsKey).contains(hit.getIp())) {
                    statsData.get(statsKey).add(hit.getIp());
                }
            } else {
                statsData.put(statsKey, new ArrayList<>(List.of(hit.getIp())));
            }
        }

        for (Map.Entry<List<String>, List<String>> statData : statsData.entrySet()) {
            statsDtoList.add(StatsDto.builder()
                    .app(statData.getKey().get(0))
                    .uri(statData.getKey().get(1))
                    .hits(statData.getValue().size())
                    .build()
            );
        }

        return statsDtoList;
    }
}
