package ru.practicum.explore.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;
import ru.practicum.explore.main.model.Category;

@UtilityClass
public class CategoryMapper {
    public CategoryDto mapCategoryToDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category mapNewDtoToCategory(NewCategoryDto newCategoryDto) {
        return Category.builder()
                .name(newCategoryDto.getName())
                .build();
    }
}
