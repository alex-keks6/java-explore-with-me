package ru.practicum.explore.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.explore.main.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "SELECT * " +
            "FROM categories AS cat " +
            "ORDER BY cat.id " +
            "OFFSET :from " +
            "LIMIT :size",
            nativeQuery = true)
    List<Category> findAllWithOffsetAndLimit(Integer from, Integer size);
}
