package ru.practicum.explore.main.controller;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CompilationDto;
import ru.practicum.explore.main.service.CompilationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @PositiveOrZero
                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                @Positive
                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@Positive @PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }
}
