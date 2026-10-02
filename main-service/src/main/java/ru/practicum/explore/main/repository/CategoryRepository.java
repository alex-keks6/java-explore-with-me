package ru.practicum.explore.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explore.main.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
