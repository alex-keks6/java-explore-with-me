package ru.practicum.explore.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.main.dto.ParticipationRequestDto;
import ru.practicum.explore.main.model.Participation;

@UtilityClass
public class ParticipationMapper {
    public ParticipationRequestDto mapParticipationToRequestDto(Participation participation) {
        return ParticipationRequestDto.builder()
                .created(participation.getCreated())
                .event(participation.getEvent().getId())
                .id(participation.getId())
                .register(participation.getRegister().getId())
                .status(participation.getStatus())
                .build();
    }
}
