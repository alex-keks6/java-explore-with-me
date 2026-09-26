package ru.practicum.explore.stats.server.service;

import ru.practicum.explore.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}
