package ru.practicum.explore.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.main.enums.State;
import ru.practicum.explore.main.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByCommenterIdOrderByCreatedOnDesc(Long userId, Pageable pageable);

    @Query("SELECT c FROM Comment c " +
            "WHERE (:users IS NULL OR c.commenter.id IN :users) " +
            "AND (:events IS NULL OR c.event.id IN :events) " +
            "AND (:states IS NULL OR c.state IN :states) " +
            "AND (cast(:rangeStart as timestamp) IS NULL OR c.createdOn >= :rangeStart) " +
            "AND (cast(:rangeEnd as timestamp) IS NULL OR c.createdOn <= :rangeEnd) " +
            "ORDER BY c.createdOn DESC")
    Page<Comment> findAllByAdminFilters(List<Long> users, List<Long> events, List<State> states,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable page);

    List<Comment> findAllByEventIdAndStateOrderByPublishedOnDesc(Long eventId, State state);
}