package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;
import ru.practicum.explore.main.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getCategories(@PositiveOrZero
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @Positive
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@Positive @PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategoryByAdmin(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.createCategoryByAdmin(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryByAdmin(@Positive @PathVariable Long catId) {
        categoryService.deleteCategoryByAdmin(catId);
    }

    @PatchMapping("/admin/categories/{catId}")
    public CategoryDto updateCategoryByAdmin(@Positive @PathVariable Long catId,
                                             @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategoryByAdmin(catId, categoryDto);
    }
}
