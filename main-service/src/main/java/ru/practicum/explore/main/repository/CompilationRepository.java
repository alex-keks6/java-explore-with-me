package ru.practicum.explore.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.main.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
    @Query(value = "SELECT * " +
            "FROM compilations AS comp " +
            "ORDER BY comp.id " +
            "OFFSET :from " +
            "LIMIT :size",
            nativeQuery = true)
    List<Compilation> findAllWithOffsetAndLimit(Integer from, Integer size);

    @Query(value = "SELECT * " +
            "FROM compilations AS comp " +
            "WHERE comp.pinned = :pinned " +
            "ORDER BY comp.id " +
            "OFFSET :from " +
            "LIMIT :size",
            nativeQuery = true)
    List<Compilation> findAllByPinnedWithOffsetAndLimit(Boolean pinned, Integer from, Integer size);
}