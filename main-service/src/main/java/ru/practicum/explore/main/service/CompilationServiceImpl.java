package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.CompilationDto;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.mapper.CompilationMapper;
import ru.practicum.explore.main.model.Compilation;
import ru.practicum.explore.main.repository.CompilationRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilationList;

        if (pinned == null) {
            compilationList = compilationRepository.findAllWithOffsetAndLimit(from, size);
        } else {
            compilationList = compilationRepository.findAllByPinnedWithOffsetAndLimit(pinned, from, size);
        }
        return compilationList.stream()
                .map(CompilationMapper::mapCompilationToDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilation(Long compId) {
        Optional<Compilation> compilation = compilationRepository.findById(compId);
        compilationRepository.existsById(compId);
        if (compilation.isEmpty()) {
            throw new DataNotFoundException("Compilation with id=" + compId + " was not found");
        }

        return CompilationMapper.mapCompilationToDto(compilation.get());
    }
}
