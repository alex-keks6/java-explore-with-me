package ru.practicum.explore.main.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.service.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/categories")
public class PublicCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@PositiveOrZero
                                           @RequestParam(required = false, defaultValue = "0") Integer from,
                                           @Positive
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@Positive @PathVariable Long catId) {
        return categoryService.getCategory(catId);
    }
}
