package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.exception.DataValidationException;
import ru.practicum.explore.main.mapper.CategoryMapper;
import ru.practicum.explore.main.model.Category;
import ru.practicum.explore.main.repository.CategoryRepository;
import ru.practicum.explore.main.repository.EventRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.mapNewDtoToCategory(newCategoryDto);

        return CategoryMapper.mapCategoryToDto(categoryRepository.save(category));
    }

    @Override
    public void deleteCategoryByAdmin(Long catId) {
        if (eventRepository.existsByCategoryId(catId)) {
            throw new DataValidationException("The category is not empty");
        }
        checkCategoryExistsById(catId);

        categoryRepository.deleteById(catId);
    }

    @Override
    public CategoryDto updateCategoryByAdmin(Long catId, CategoryDto categoryDto) {
        checkCategoryExistsById(catId);

        categoryDto.setId(catId);
        Category category = CategoryMapper.mapDtoToCategory(categoryDto);

        return CategoryMapper.mapCategoryToDto(categoryRepository.save(category));
    }

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        List<Category> categoryList = categoryRepository.findAllWithOffsetAndLimit(from, size);
        return categoryList.stream()
                .map(CategoryMapper::mapCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(Long catId) {
        Category category = takeCategoryById(catId);

        return CategoryMapper.mapCategoryToDto(category);
    }

    @Override
    public Category takeCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new DataNotFoundException("Category with id=" + categoryId + " was not found"));
    }

    private void checkCategoryExistsById(Long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new DataNotFoundException("Category with id=" + categoryId + " was not found");
        }
    }
}