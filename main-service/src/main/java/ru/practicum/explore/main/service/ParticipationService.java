package ru.practicum.explore.main.service;

import ru.practicum.explore.main.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationService {
    List<ParticipationRequestDto> getOwnParticipationsByUser(Long userId);

    ParticipationRequestDto createParticipationByUser(Long userId, Long eventId);

    ParticipationRequestDto cancelOwnParticipationByUser(Long userId, Long requestId);
}
