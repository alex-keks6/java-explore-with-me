package ru.practicum.explore.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.main.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
}
