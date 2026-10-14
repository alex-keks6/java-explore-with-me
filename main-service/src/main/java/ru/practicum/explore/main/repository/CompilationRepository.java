package ru.practicum.explore.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.main.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query("SELECT comp " +
            "FROM Compilation comp " +
            "ORDER BY comp.id")
    Page<Compilation> findAllWithOffsetAndLimit(Pageable pageable);

    @Query("SELECT comp " +
            "FROM Compilation comp " +
            "WHERE comp.pinned = :pinned " +
            "ORDER BY comp.id")
    Page<Compilation> findAllByPinnedWithOffsetAndLimit(Boolean pinned, Pageable pageable);
}