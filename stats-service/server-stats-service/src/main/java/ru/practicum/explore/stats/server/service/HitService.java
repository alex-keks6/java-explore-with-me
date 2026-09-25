package ru.practicum.explore.stats.server.service;

import ru.practicum.explore.stats.dto.HitDto;

public interface HitService {
    void saveHit(HitDto hitDto);
}
