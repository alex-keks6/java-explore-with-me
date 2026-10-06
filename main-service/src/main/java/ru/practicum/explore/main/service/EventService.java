package ru.practicum.explore.main.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explore.main.dto.*;
import ru.practicum.explore.main.enums.Sort;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.request.EventRequestStatusUpdateRequest;
import ru.practicum.explore.main.request.EventRequestStatusUpdateResult;
import ru.practicum.explore.main.request.UpdateEventAdminRequest;
import ru.practicum.explore.main.request.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> getEvents(PublicEventSearchParameterDto searchParameter, HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<EventShortDto> getOwnEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto createEventByUser(Long userId, NewEventDto newEventDto);

    EventFullDto getOwnEventByUser(Long userId, Long eventId);

    EventFullDto updateOwnEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest);

    List<ParticipationRequestDto> getOwnEventRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateOwnEventRequestsByUser(Long userId, Long eventId,
                                                                EventRequestStatusUpdateRequest
                                                                        eventRequestStatusUpdateRequest);

    List<EventFullDto> getEventsByAdmin(AdminEventSearchParameterDto searchParameter);

    EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    Event takeEventById(Long eventId);
}