package ru.practicum.explore.main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.EventFullDto;
import ru.practicum.explore.main.dto.EventShortDto;
import ru.practicum.explore.main.dto.NewEventDto;
import ru.practicum.explore.main.dto.ParticipationRequestDto;
import ru.practicum.explore.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore.main.request.EventRequestStatusUpdateResult;
import ru.practicum.explore.main.request.UpdateEventUserRequest;
import ru.practicum.explore.main.service.EventService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getOwnEventsByUser(@Positive @PathVariable Long userId,
                                                  @PositiveOrZero
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @Positive
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getOwnEventsByUser(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventByUser(@Positive @PathVariable Long userId,
                                          @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.createEventByUser(userId, newEventDto);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getOwnEventByUser(@Positive @PathVariable Long userId,
                                          @Positive @PathVariable Long eventId) {
        return eventService.getOwnEventByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateOwnEventByUser(@Positive @PathVariable Long userId,
                                             @Positive @PathVariable Long eventId,
                                             @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateOwnEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getOwnEventRequestsByUser(@Positive @PathVariable Long userId,
                                                                   @Positive @PathVariable Long eventId) {
        return eventService.getOwnEventRequestsByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateOwnEventRequestsByUser(@Positive @PathVariable Long userId,
                                                                       @Positive @PathVariable Long eventId,
                                                                       @Valid
                                                                       @RequestBody
                                                                       EventRequestStatusUpdateRequest
                                                                               eventRequestStatusUpdateRequest) {
        return eventService.updateOwnEventRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
