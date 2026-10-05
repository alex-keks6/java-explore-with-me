package ru.practicum.explore.main.service;

import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;
import ru.practicum.explore.main.model.Category;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto);

    void deleteCategoryByAdmin(Long catId);

    CategoryDto updateCategoryByAdmin(Long catId, CategoryDto categoryDto);

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategory(Long catId);

    Category takeCategoryById(Long categoryId);
}
