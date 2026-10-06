package ru.practicum.explore.main.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.*;
import ru.practicum.explore.main.enums.*;
import ru.practicum.explore.main.exception.DataBadRequestException;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.exception.DataValidationException;
import ru.practicum.explore.main.mapper.EventMapper;
import ru.practicum.explore.main.mapper.ParticipationMapper;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.model.Participation;
import ru.practicum.explore.main.repository.EventRepository;
import ru.practicum.explore.main.repository.ParticipationRepository;
import ru.practicum.explore.main.request.*;
import ru.practicum.explore.stats.client.HitClient;
import ru.practicum.explore.stats.client.StatsClient;
import ru.practicum.explore.stats.dto.StatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String EVENT_URI = "/events";
    private final HitClient hitClient;
    private final StatsClient statsClient;
    private final UserService userService;
    private final CategoryService categoryService;
    private final EventRepository eventRepository;
    private final ParticipationRepository participationRepository;

    @Override
    public List<EventShortDto> getEvents(PublicEventSearchParameterDto searchParameter, HttpServletRequest request) {
        if (searchParameter.getRangeStart() == null && searchParameter.getRangeEnd() == null) {
            searchParameter.setRangeStart(LocalDateTime.now());
        }

        PageRequest page = PageRequest.of(searchParameter.getFrom() > 0 ?
                searchParameter.getFrom() / searchParameter.getSize() : 0, searchParameter.getSize());
        List<Event> eventList;
        if (searchParameter.getSort().equals(Sort.EVENT_DATE)) {
            eventList = eventRepository.findAllByFiltersSortedByEventDate(EventState.PUBLISHED,
                    searchParameter.getText(), searchParameter.getCategories(), searchParameter.getPaid(),
                    searchParameter.getRangeStart(), searchParameter.getRangeEnd(), searchParameter.getOnlyAvailable(),
                    page).getContent();
        } else {
            // Так как в бд больше не хранится информация о просмотрах, то теперь из бд данные берутся без сортировки
            // и уже в сервисе сортируются. Если можно лучше, то скажите, исправлю.
            eventList = eventRepository.findAllByFilters(EventState.PUBLISHED, searchParameter.getText(),
                    searchParameter.getCategories(), searchParameter.getPaid(), searchParameter.getRangeStart(),
                    searchParameter.getRangeEnd(), searchParameter.getOnlyAvailable(), page).getContent();
        }

        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(EventMapper::mapEventToShortDto)
                .collect(Collectors.toList());

        sendStatistic(request);

        LocalDateTime earliestPublishedOn = takeEarliestPublishedOn(eventList);

        setViewsInEventShortDtoList(eventShortDtoList, earliestPublishedOn);

        if (searchParameter.getSort().equals(Sort.VIEWS)) {
            eventShortDtoList.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        }

        return eventShortDtoList;
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = takeEventById(eventId);

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new DataNotFoundException("Event must be published");
        }

        EventFullDto eventFullDto = EventMapper.mapEventToFullDto(event);

        sendStatistic(request);

        setViewInEventFullDto(eventFullDto);

        return eventFullDto;
    }

    @Override
    public List<EventShortDto> getOwnEventsByUser(Long userId, Integer from, Integer size) {
        List<Event> eventList = eventRepository.findAllByUserIdWithOffsetAndLimit(userId, from, size);

        List<EventShortDto> eventShortDtoList = eventList.stream()
                .map(EventMapper::mapEventToShortDto)
                .collect(Collectors.toList());

        LocalDateTime earliestPublishedOn = takeEarliestPublishedOn(eventList);

        setViewsInEventShortDtoList(eventShortDtoList, earliestPublishedOn);

        return eventShortDtoList;
    }

    @Override
    public EventFullDto createEventByUser(Long userId, NewEventDto newEventDto) {
        checkCorrectNewEventDate(newEventDto.getEventDate());

        Event event = EventMapper.mapNewDtoToEvent(newEventDto);
        event.setInitiator(userService.takeUserById(userId));
        event.setCategory(categoryService.takeCategoryById(newEventDto.getCategory()));
        event.setCreatedOn(LocalDateTime.now());
        event.setState(EventState.PENDING);

        return EventMapper.mapEventToFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto getOwnEventByUser(Long userId, Long eventId) {
        Event event = takeEventByUserIdAndId(userId, eventId);

        EventFullDto eventFullDto = EventMapper.mapEventToFullDto(event);

        setViewInEventFullDto(eventFullDto);

        return eventFullDto;
    }

    @Override
    public EventFullDto updateOwnEventByUser(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        checkCorrectNewEventDate(updateEventUserRequest.getEventDate());

        Event event = takeEventByUserIdAndId(userId, eventId);

        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new DataValidationException("Only pending or canceled events can be changed");
        }

        updateEventByRequest(event, updateEventUserRequest);

        if (updateEventUserRequest.getStateAction() != null) {
            switch (updateEventUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(EventState.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        if (updateEventUserRequest.getTitle() != null) {
            event.setTitle(updateEventUserRequest.getTitle());
        }

        EventFullDto eventFullDto = EventMapper.mapEventToFullDto(event);

        setViewInEventFullDto(eventFullDto);

        return eventFullDto;
    }

    @Override
    public List<ParticipationRequestDto> getOwnEventRequestsByUser(Long userId, Long eventId) {
        if (!eventRepository.existsByInitiatorIdAndId(userId, eventId)) {
            throw new DataNotFoundException("Event with id=" + eventId + " and initiatorId=" +
                    userId + " was not found");
        }

        List<Participation> participationList = participationRepository.findAllByEventId(eventId);

        return participationList.stream()
                .map(ParticipationMapper::mapParticipationToRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateOwnEventRequestsByUser(Long userId, Long eventId,
                                                                       EventRequestStatusUpdateRequest
                                                                               eventRequestStatusUpdateRequest) {
        Event event = takeEventByUserIdAndId(userId, eventId);

        EventRequestStatusUpdateResult result = EventRequestStatusUpdateResult.builder()
                .confirmedRequests(new ArrayList<>())
                .rejectedRequests(new ArrayList<>())
                .build();

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return result;
        }

        Long participationConfirmedCount = event.getConfirmedRequests();

        if (event.getParticipantLimit() != 0 && participationConfirmedCount >= event.getParticipantLimit()) {
            throw new DataValidationException("The participant limit has been reached");
        }

        List<Participation> participationList = participationRepository.findAllByEventIdAndIdIn(eventId,
                eventRequestStatusUpdateRequest.getRequestIds());

        if (participationList.size() != eventRequestStatusUpdateRequest.getRequestIds().size()) {
            throw new DataValidationException("Incorrect requestIds");
        }

        for (Participation participation : participationList) {
            if (!participation.getStatus().equals(ParticipationStatus.PENDING)) {
                throw new DataValidationException("Request must have status PENDING");
            }
        }

        if (eventRequestStatusUpdateRequest.getStatus() == ParticipationUpdateStatus.REJECTED) {
            for (Participation participation : participationList) {
                participation.setStatus(ParticipationStatus.REJECTED);
                result.getRejectedRequests().add(ParticipationMapper.mapParticipationToRequestDto(participation));
            }
        } else {
            Long freeParticipants = event.getParticipantLimit() - participationConfirmedCount;

            for (Participation participation : participationList) {
                if (freeParticipants > 0) {
                    participation.setStatus(ParticipationStatus.CONFIRMED);
                    result.getConfirmedRequests().add(ParticipationMapper.mapParticipationToRequestDto(participation));
                    freeParticipants--;
                } else {
                    participation.setStatus(ParticipationStatus.REJECTED);
                    result.getRejectedRequests().add(ParticipationMapper.mapParticipationToRequestDto(participation));
                }
            }
            event.setConfirmedRequests(event.getConfirmedRequests() + result.getConfirmedRequests().size());
            eventRepository.save(event);
        }

        participationRepository.saveAll(participationList);

        return result;
    }

    @Override
    public List<EventFullDto> getEventsByAdmin(AdminEventSearchParameterDto searchParameter) {
        PageRequest page = PageRequest.of(searchParameter.getFrom() > 0 ?
                searchParameter.getFrom() / searchParameter.getSize() : 0, searchParameter.getSize());

        List<Event> eventList = eventRepository.findAllByFiltersByAdmin(searchParameter.getUsers(),
                searchParameter.getStates(), searchParameter.getCategories(), searchParameter.getRangeStart(),
                searchParameter.getRangeEnd(), page).getContent();

        List<EventFullDto> eventFullDtoList = eventList.stream()
                .map(EventMapper::mapEventToFullDto)
                .collect(Collectors.toList());

        LocalDateTime earliestPublishedOn = takeEarliestPublishedOn(eventList);

        setViewsInEventFullDtoList(eventFullDtoList, earliestPublishedOn);

        return eventFullDtoList;
    }

    @Override
    public EventFullDto updateEventByAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event event = takeEventById(eventId);

        if (!event.getState().equals(EventState.PENDING)
                && updateEventAdminRequest.getStateAction() == EventStateUpdate.PUBLISH_EVENT) {
            throw new DataValidationException("Cannot publish the event because it's not in the right state: " +
                    event.getState());
        }

        if (event.getState().equals(EventState.PUBLISHED)
                && updateEventAdminRequest.getStateAction() == EventStateUpdate.REJECT_EVENT) {
            throw new DataValidationException("Cannot reject the event because it's not in the right state: " +
                    event.getState());
        }

        LocalDateTime eventDate = event.getEventDate();
        if (updateEventAdminRequest.getEventDate() != null) {
            eventDate = updateEventAdminRequest.getEventDate();
        }

        LocalDateTime publishedOn = LocalDateTime.now();

        if (eventDate.isBefore(publishedOn.plusHours(1))) {
            throw new DataBadRequestException("The eventDate is incorrect relative to the publishedOn. " +
                    "publishedOn=" + publishedOn + ", eventDate=" + eventDate);
        }

        updateEventByRequest(event, updateEventAdminRequest);

        if (updateEventAdminRequest.getStateAction() != null) {
            switch (updateEventAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(publishedOn);
                    break;
                case REJECT_EVENT:
                    event.setState(EventState.CANCELED);
                    break;
            }
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }

        EventFullDto eventFullDto = EventMapper.mapEventToFullDto(eventRepository.save(event));

        setViewInEventFullDto(eventFullDto);

        return eventFullDto;
    }

    @Override
    public Event takeEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() ->
                new DataNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Event takeEventByUserIdAndId(Long userId, Long eventId) {
        return eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(() ->
                new DataNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private void checkCorrectNewEventDate(LocalDateTime newEventDate) {
        LocalDateTime currentMoment = LocalDateTime.now();

        if (newEventDate != null && newEventDate.isBefore(currentMoment.plusHours(2))) {
            throw new DataBadRequestException("Field: eventDate. " +
                    "Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + newEventDate);
        }
    }

    private void setViewsInEventFullDtoList(List<EventFullDto> eventFullDtoList, LocalDateTime earliestPublishedOn) {
        List<String> uriList = new ArrayList<>();
        for (EventFullDto eventFullDto : eventFullDtoList) {
            String uri = EVENT_URI + "/" + eventFullDto.getId();
            uriList.add(uri);
        }

        List<StatsDto> stats = getStatsDto(earliestPublishedOn, uriList);

        for (EventFullDto eventFullDto : eventFullDtoList) {
            for (StatsDto stat : stats) {
                if (Long.parseLong(stat.getUri().substring(stat.getUri().lastIndexOf("/") + 1))
                        == eventFullDto.getId()) {
                    eventFullDto.setViews(stat.getHits());
                }
            }
        }
    }

    private void setViewsInEventShortDtoList(List<EventShortDto> eventShortDtoList, LocalDateTime earliestPublishedOn) {
        List<String> uriList = new ArrayList<>();
        for (EventShortDto eventShortDto : eventShortDtoList) {
            String uri = EVENT_URI + "/" + eventShortDto.getId();
            uriList.add(uri);
        }

        List<StatsDto> stats = getStatsDto(earliestPublishedOn, uriList);

        for (EventShortDto eventShortDto : eventShortDtoList) {
            for (StatsDto stat : stats) {
                if (Long.parseLong(stat.getUri().substring(stat.getUri().lastIndexOf("/") + 1))
                        == eventShortDto.getId()) {
                    eventShortDto.setViews(stat.getHits());
                }
            }
        }
    }

    private void setViewInEventFullDto(EventFullDto eventFullDto) {
        if (eventFullDto.getPublishedOn() == null) {
            return;
        }

        List<StatsDto> stats = getStatsDto(eventFullDto.getPublishedOn(), List.of(EVENT_URI));

        if (stats.getFirst() != null) {
            eventFullDto.setViews(stats.getFirst().getHits());
        }
    }

    private void sendStatistic(HttpServletRequest request) {
        hitClient.saveHit(request);
    }

    private LocalDateTime takeEarliestPublishedOn(List<Event> eventList) {
        LocalDateTime earliestPublishedOn = LocalDateTime.now();
        for (Event event : eventList) {
            if (event.getPublishedOn() != null && event.getPublishedOn().isBefore(earliestPublishedOn)) {
                earliestPublishedOn = event.getPublishedOn();
            }
        }
        return earliestPublishedOn;
    }

    private List<StatsDto> getStatsDto(LocalDateTime earliestPublishedOn, List<String> uriList) {
        return statsClient.getStats(earliestPublishedOn, LocalDateTime.now(), uriList, true);
    }

    private void updateEventByRequest(Event event, UpdateEventRequest updateEventRequest) {
        if (updateEventRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getCategory() != null) {
            event.setCategory(categoryService.takeCategoryById(updateEventRequest.getCategory()));
        }
        if (updateEventRequest.getDescription() != null) {
            event.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            event.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getLocation() != null) {
            event.setLat(updateEventRequest.getLocation().getLat());
            event.setLon(updateEventRequest.getLocation().getLon());
        }
        if (updateEventRequest.getPaid() != null) {
            event.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(updateEventRequest.getRequestModeration());
        }
    }
}