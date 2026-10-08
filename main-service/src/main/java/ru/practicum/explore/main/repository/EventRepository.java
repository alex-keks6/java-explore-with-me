package ru.practicum.explore.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.main.enums.State;
import ru.practicum.explore.main.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e FROM Event e " +
            "WHERE e.state = :state " +
            "AND (cast(:text as string) IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', cast(:text as string), '%')) " +
            "                   OR LOWER(e.description) LIKE LOWER(CONCAT('%', cast(:text as string), '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (cast(:rangeStart as timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd as timestamp) IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND (:onlyAvailable = false OR e.confirmedRequests < e.participantLimit) " +
            "ORDER BY e.eventDate")
    Page<Event> findAllByFiltersSortedByEventDate(State state, String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable,
                                                  Pageable pageable);

    @Query("SELECT e FROM Event e " +
            "WHERE e.state = :state " +
            "AND (cast(:text as string) IS NULL OR LOWER(e.annotation) LIKE LOWER(CONCAT('%', cast(:text as string), '%')) " +
            "                   OR LOWER(e.description) LIKE LOWER(CONCAT('%', cast(:text as string), '%'))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (cast(:rangeStart as timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd as timestamp) IS NULL OR e.eventDate <= :rangeEnd) " +
            "AND (:onlyAvailable = false OR e.confirmedRequests < e.participantLimit)")
    Page<Event> findAllByFilters(State state, String text, List<Long> categories, Boolean paid,
                                 LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                 Boolean onlyAvailable, Pageable pageable);

    Boolean existsByCategoryId(Long categoryId);

    @Query(value = "SELECT * " +
            "FROM events AS ev " +
            "WHERE ev.user_id = :userId " +
            "ORDER BY ev.event_date " +
            "OFFSET :from " +
            "LIMIT :size",
            nativeQuery = true)
    List<Event> findAllByUserIdWithOffsetAndLimit(Long userId, Integer from, Integer size);

    Optional<Event> findByInitiatorIdAndId(Long userId, Long eventId);

    Boolean existsByInitiatorIdAndId(Long userId, Long eventId);

    @Query("SELECT e FROM Event e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (cast(:rangeStart as timestamp) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd as timestamp) IS NULL OR e.eventDate <= :rangeEnd)")
    Page<Event> findAllByFiltersByAdmin(List<Long> users, List<String> states, List<Long> categories,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    List<Event> findAllByCompilationId(Long id);
}