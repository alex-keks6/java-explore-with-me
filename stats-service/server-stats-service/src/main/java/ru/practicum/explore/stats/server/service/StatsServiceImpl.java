package ru.practicum.explore.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.dto.StatsDto;
import ru.practicum.explore.stats.server.exception.ValidationException;
import ru.practicum.explore.stats.server.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public List<StatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (start.isAfter(end)) {
            throw new ValidationException("Дата и время начала диапазона поиска " +
                    "не может быть позже даты и времени конца.");
        }

        List<StatsDto> statsDtoList;
        List<String> urisMaybeNull = null;
        if (uris != null && !uris.isEmpty()) {
            urisMaybeNull = uris;
        }

        if (unique) {
            statsDtoList = statsRepository.countUriAndAppByIpUnique(start, end, urisMaybeNull);
        } else {
            statsDtoList = statsRepository.countUriAndAppByIp(start, end, urisMaybeNull);
        }

        return statsDtoList;
    }
}
