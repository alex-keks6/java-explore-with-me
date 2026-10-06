package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CompilationDto;
import ru.practicum.explore.main.dto.NewCompilationDto;
import ru.practicum.explore.main.request.UpdateCompilationRequest;
import ru.practicum.explore.main.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto createCompilationByAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        return compilationService.createCompilationByAdmin(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilationByAdmin(@Positive @PathVariable Long compId) {
        compilationService.deleteCompilationByAdmin(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilationByAdmin(@Positive @PathVariable Long compId,
                                                   @Valid
                                                   @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        return compilationService.updateCompilationByAdmin(compId, updateCompilationRequest);
    }
}