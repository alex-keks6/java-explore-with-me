package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;
import ru.practicum.explore.main.mapper.CategoryMapper;
import ru.practicum.explore.main.model.Category;
import ru.practicum.explore.main.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.mapNewDtoToCategory(newCategoryDto);
        return CategoryMapper.mapCategoryToDto(categoryRepository.save(category));
    }
}
