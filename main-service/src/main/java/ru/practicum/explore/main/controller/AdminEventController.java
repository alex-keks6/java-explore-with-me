package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.AdminEventSearchParameterDto;
import ru.practicum.explore.main.dto.EventFullDto;
import ru.practicum.explore.main.request.UpdateEventAdminRequest;
import ru.practicum.explore.main.service.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
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
        AdminEventSearchParameterDto searchParameter = new AdminEventSearchParameterDto(users, states, categories,
                rangeStart, rangeEnd, from, size);
        return eventService.getEventsByAdmin(searchParameter);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable Long eventId,
                                           @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }
}