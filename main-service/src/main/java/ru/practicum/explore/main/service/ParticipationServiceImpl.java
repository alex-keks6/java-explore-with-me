package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.ParticipationRequestDto;
import ru.practicum.explore.main.enums.EventState;
import ru.practicum.explore.main.enums.ParticipationStatus;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.exception.DataValidationException;
import ru.practicum.explore.main.mapper.ParticipationMapper;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.model.Participation;
import ru.practicum.explore.main.repository.ParticipationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationServiceImpl implements ParticipationService {
    private final UserService userService;
    private final EventService eventService;
    private final ParticipationRepository participationRepository;

    @Override
    public List<ParticipationRequestDto> getOwnParticipationsByUser(Long userId) {
        userService.checkExistsUserById(userId);

        List<Participation> participationList = participationRepository.findAllByRegisterId(userId);
        return participationList.stream()
                .map(ParticipationMapper::mapParticipationToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ParticipationRequestDto createParticipationByUser(Long userId, Long eventId) {
        if (participationRepository.existsByRegisterIdAndEventId(userId, eventId)) {
            throw new DataValidationException("Participation already exists");
        }

        Event event = eventService.takeEventById(eventId);

        if (event.getInitiator().getId().equals(userId)) {
            throw new DataValidationException("Initiator cannot add a participation in their own event");
        }

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataValidationException("Event is not PUBLISHED");
        }

        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() - event.getConfirmedRequests() <= 0) {
            throw new DataValidationException("The participant limit has been reached");
        }

        ParticipationStatus participationStatus;

        if (event.getRequestModeration()) {
            participationStatus = ParticipationStatus.PENDING;
        } else {
            participationStatus = ParticipationStatus.CONFIRMED;
        }

        Participation participation = Participation.builder()
                .created(LocalDateTime.now())
                .event(event)
                .register(userService.takeUserById(userId))
                .status(participationStatus)
                .build();

        return ParticipationMapper.mapParticipationToRequestDto(participationRepository.save(participation));
    }

    @Override
    public ParticipationRequestDto cancelOwnParticipationByUser(Long userId, Long requestId) {
        Participation participation = takeParticipationById(requestId);

        participation.setStatus(ParticipationStatus.REJECTED);

        return ParticipationMapper.mapParticipationToRequestDto(participationRepository.save(participation));
    }

    private Participation takeParticipationById(Long requestId) {
        Optional<Participation> optionalParticipation = participationRepository.findById(requestId);
        if (optionalParticipation.isEmpty()) {
            throw new DataNotFoundException("Request with id=" + requestId + " was not found");
        }
        return optionalParticipation.get();
    }
}