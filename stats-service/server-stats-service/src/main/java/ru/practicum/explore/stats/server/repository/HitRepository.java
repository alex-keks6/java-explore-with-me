package ru.practicum.explore.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.stats.server.model.Hit;

public interface HitRepository extends JpaRepository<Hit, Long> {
}
