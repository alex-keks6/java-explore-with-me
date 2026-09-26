package ru.practicum.explore.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.server.mapper.HitMapper;
import ru.practicum.explore.stats.server.repository.HitRepository;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;

    @Override
    public void saveHit(HitDto hitDto) {
        hitRepository.save(HitMapper.mapDtoToHit(hitDto));
    }
}
