package ru.practicum.explore.stats.server.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.dto.HitDto;
import ru.practicum.explore.stats.server.mapper.HitMapper;
import ru.practicum.explore.stats.server.repository.HitRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {
    private final HitRepository hitRepository;
    @Value("${main-service.name}")
    private final String app;

    @Override
    public void saveHit(HttpServletRequest request) {
        HitDto hitDto = HitDto.builder()
                .app(app)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now())
                .build();
        hitRepository.save(HitMapper.mapDtoToHit(hitDto));
    }
}