package ru.practicum.explore.main.service;

import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;

public interface CategoryService {
    CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto);
}
