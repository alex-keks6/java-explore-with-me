package ru.practicum.explore.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.explore.main.enums.ParticipationStatus;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestDto {
    private LocalDateTime created;
    private Long event;
    private Long id;
    private Long register;
    private ParticipationStatus status;

}
