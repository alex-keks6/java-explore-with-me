package ru.practicum.explore.main.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explore.main.dto.EventFullDto;
import ru.practicum.explore.main.dto.EventShortDto;
import ru.practicum.explore.main.dto.NewEventDto;
import ru.practicum.explore.main.dto.ParticipationRequestDto;
import ru.practicum.explore.main.enums.Sort;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore.main.request.EventRequestStatusUpdateResult;
import ru.practicum.explore.main.request.UpdateEventAdminRequest;
import ru.practicum.explore.main.request.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, Sort sort, Integer from, Integer size,
                                  HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getOwnEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto createEventByUser(Long userId, NewEventDto newEventDto);

    EventFullDto getOwnEventByUser(Long userId, Long eventId);

    EventFullDto updateOwnEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getOwnEventRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateOwnEventRequestsByUser(Long userId, Long eventId,
                                                                EventRequestStatusUpdateRequest
                                                                        eventRequestStatusUpdateRequest);

    List<EventFullDto> getEventsByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Event takeEventById(Long eventId);
}