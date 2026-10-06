package ru.practicum.explore.main.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.ParticipationRequestDto;
import ru.practicum.explore.main.service.ParticipationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/requests")
public class PrivateParticipationController {
    private final ParticipationService participationService;

    @GetMapping
    public List<ParticipationRequestDto> getOwnParticipationsByUser(@Positive @PathVariable Long userId) {
        return participationService.getOwnParticipationsByUser(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createParticipationByUser(@Positive @PathVariable Long userId,
                                                             @Positive @RequestParam Long eventId) {
        return participationService.createParticipationByUser(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancelOwnParticipationByUser(@Positive @PathVariable Long userId,
                                                                @Positive @PathVariable Long requestId) {
        return participationService.cancelOwnParticipationByUser(userId, requestId);
    }
}