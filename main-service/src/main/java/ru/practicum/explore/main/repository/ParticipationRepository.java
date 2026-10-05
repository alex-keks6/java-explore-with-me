package ru.practicum.explore.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.main.model.Participation;

import java.util.List;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
    List<Participation> findAllByEventId(Long eventId);

    List<Participation> findAllByEventIdAndIdIn(Long eventId, List<Long> requestIds);

    List<Participation> findAllByRegisterId(Long userId);

    Boolean existsByRegisterIdAndEventId(Long userId, Long eventId);

}
