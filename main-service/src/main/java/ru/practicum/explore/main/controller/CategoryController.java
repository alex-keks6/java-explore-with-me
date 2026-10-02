package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CategoryDto;
import ru.practicum.explore.main.dto.NewCategoryDto;
import ru.practicum.explore.main.service.CategoryService;

@RestController
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategoryByAdmin(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.createCategoryByAdmin(newCategoryDto);
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategoryByAdmin() {

    }
}
