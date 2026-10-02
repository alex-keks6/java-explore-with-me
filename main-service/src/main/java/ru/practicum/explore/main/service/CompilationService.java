package ru.practicum.explore.main.service;

import jakarta.validation.constraints.Positive;
import ru.practicum.explore.main.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(@Positive Long compId);
}
