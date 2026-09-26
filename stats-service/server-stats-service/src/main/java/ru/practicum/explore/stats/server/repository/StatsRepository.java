package ru.practicum.explore.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.stats.server.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    /// Не придумал, как с помощью запроса получить готовый List<StatsDto>.
    /// Так и не понял, как можно построить запрос, чтоб в нем в зависимости от логического флага unique
    /// отбрасывались либо оставались строки, в которых для пары uri, app значения ip одинаковое
    /// и чтоб потом они группировались. Если есть адекватное решение, то подскажите, пожалуйста, исправлю.

    List<Hit> findAllByTimestampBetween(LocalDateTime start, LocalDateTime end);
}
