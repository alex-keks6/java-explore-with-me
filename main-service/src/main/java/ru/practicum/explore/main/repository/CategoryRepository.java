package ru.practicum.explore.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.main.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT cat " +
            "FROM Category cat " +
            "ORDER BY cat.id")
    Page<Category> findAllWithOffsetAndLimit(Pageable pageable);
}