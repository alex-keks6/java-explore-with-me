package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
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
@RequestMapping(path = "/admin/categories")
public class AdminCategoryController {
    // Разделил каждый controller на public, private и admin. То есть разделение и по сущности, и по уровню доступа.
    // Если неправильно понял логику и нужно было по итогу объединить все ручки в три контроллера по уровню доступа
    // (public, private, admin), то скажите, исправлю.
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategoryByAdmin(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        return categoryService.createCategoryByAdmin(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoryByAdmin(@Positive @PathVariable Long catId) {
        categoryService.deleteCategoryByAdmin(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDto updateCategoryByAdmin(@Positive @PathVariable Long catId,
                                             @Valid @RequestBody CategoryDto categoryDto) {
        return categoryService.updateCategoryByAdmin(catId, categoryDto);
    }
}