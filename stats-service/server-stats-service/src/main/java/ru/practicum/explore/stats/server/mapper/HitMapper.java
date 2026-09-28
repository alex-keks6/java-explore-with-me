package ru.practicum.explore.stats.server.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.server.model.Hit;

@UtilityClass
public class HitMapper {
    public HitDto mapHitToDto(Hit hit) {
        return HitDto.builder()
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .timestamp(hit.getTimestamp())
                .build();
    }

    public Hit mapDtoToHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .timestamp(hitDto.getTimestamp())
                .build();
    }
}
