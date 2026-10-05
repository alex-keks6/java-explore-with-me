package ru.practicum.explore.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.stats.dto.StatsDto;
import ru.practicum.explore.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.explore.stats.dto.StatsDto(h.app, h.uri, count(h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between :start and :end " +
            "and (:urisMaybeNull is null or h.uri in :urisMaybeNull) " +
            "group by h.app, h.uri " +
            "order by count(h.ip) desc")
    List<StatsDto> countUriAndAppByIp(LocalDateTime start, LocalDateTime end, List<String> urisMaybeNull);

    @Query("select new ru.practicum.explore.stats.dto.StatsDto(h.app, h.uri, count(distinct h.ip)) " +
            "from Hit as h " +
            "where h.timestamp between :start and :end " +
            "and (:urisMaybeNull is null or h.uri in :urisMaybeNull) " +
            "group by h.app, h.uri " +
            "order by count(distinct h.ip) desc")
    List<StatsDto> countUriAndAppByIpUnique(LocalDateTime start, LocalDateTime end, List<String> urisMaybeNull);
}