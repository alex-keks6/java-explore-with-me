package ru.practicum.explore.main.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.EventFullDto;
import ru.practicum.explore.main.dto.EventShortDto;
import ru.practicum.explore.main.dto.NewEventDto;
import ru.practicum.explore.main.dto.ParticipationRequestDto;
import ru.practicum.explore.main.enums.Sort;
import ru.practicum.explore.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore.main.request.EventRequestStatusUpdateResult;
import ru.practicum.explore.main.request.UpdateEventAdminRequest;
import ru.practicum.explore.main.request.UpdateEventUserRequest;
import ru.practicum.explore.main.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class EventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                         @RequestParam(required = false) List<@Positive Long> categories,
                                         @RequestParam(required = false) Boolean paid,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         @RequestParam(required = false) LocalDateTime rangeStart,
                                         @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                         @RequestParam(required = false) LocalDateTime rangeEnd,
                                         @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(required = false, defaultValue = "EVENT_DATE") Sort sort,
                                         @PositiveOrZero
                                         @RequestParam(required = false, defaultValue = "0") Integer from,
                                         @Positive
                                         @RequestParam(required = false, defaultValue = "10") Integer size,
                                         HttpServletRequest request) {
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size,
                request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@Positive @PathVariable Long id,
                                 HttpServletRequest request) {
        return eventService.getEvent(id, request);
    }


    @GetMapping("/users/{userId}/events")
    public List<EventShortDto> getOwnEventsByUser(@Positive @PathVariable Long userId,
                                                  @PositiveOrZero
                                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                                  @Positive
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getOwnEventsByUser(userId, from, size);
    }

    @PostMapping("/users/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEventByUser(@Positive @PathVariable Long userId,
                                          @Valid @RequestBody NewEventDto newEventDto) {
        return eventService.createEventByUser(userId, newEventDto);
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public EventFullDto getOwnEventByUser(@Positive @PathVariable Long userId,
                                          @Positive @PathVariable Long eventId) {
        return eventService.getOwnEventByUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public EventFullDto updateOwnEventByUser(@Positive @PathVariable Long userId,
                                             @Positive @PathVariable Long eventId,
                                             @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        return eventService.updateOwnEventByUser(userId, eventId, updateEventUserRequest);
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getOwnEventRequestsByUser(@Positive @PathVariable Long userId,
                                                                   @Positive @PathVariable Long eventId) {
        return eventService.getOwnEventRequestsByUser(userId, eventId);
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateOwnEventRequestsByUser(@Positive @PathVariable Long userId,
                                                                       @Positive @PathVariable Long eventId,
                                                                       @Valid
                                                                       @RequestBody
                                                                       EventRequestStatusUpdateRequest
                                                                               eventRequestStatusUpdateRequest) {
        return eventService.updateOwnEventRequestsByUser(userId, eventId, eventRequestStatusUpdateRequest);
    }

    @GetMapping("/admin/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) List<@Positive Long> users,
                                               @RequestParam(required = false) List<String> states,
                                               @RequestParam(required = false) List<@Positive Long> categories,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               @RequestParam(required = false) LocalDateTime rangeStart,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               @RequestParam(required = false) LocalDateTime rangeEnd,
                                               @PositiveOrZero
                                               @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Positive
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/admin/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}
