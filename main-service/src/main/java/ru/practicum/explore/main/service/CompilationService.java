package ru.practicum.explore.main.service;

import ru.practicum.explore.main.dto.CompilationDto;
import ru.practicum.explore.main.dto.NewCompilationDto;
import ru.practicum.explore.main.request.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compId);

    CompilationDto createCompilationByAdmin(NewCompilationDto newCompilationDto);

    void deleteCompilationByAdmin(Long compId);

    CompilationDto updateCompilationByAdmin(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
