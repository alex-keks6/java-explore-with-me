package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.CompilationDto;
import ru.practicum.explore.main.dto.NewCompilationDto;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.mapper.CompilationMapper;
import ru.practicum.explore.main.model.Compilation;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.repository.CompilationRepository;
import ru.practicum.explore.main.repository.EventRepository;
import ru.practicum.explore.main.request.UpdateCompilationRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;


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

    @Override
    public CompilationDto createCompilationByAdmin(NewCompilationDto newCompilationDto) {
        Compilation compilation = Compilation.builder()
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();

        compilation = compilationRepository.save(compilation);

        if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
            List<Event> newEventList = eventRepository.findAllById(newCompilationDto.getEvents());
            updateCompilationAndEvents(compilation, newEventList);
        }

        return CompilationMapper.mapCompilationToDto(compilation);
    }

    @Override
    public void deleteCompilationByAdmin(Long compId) {
        checkExistsCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationByAdmin(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = takeCompilationById(compId);

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        compilation = compilationRepository.save(compilation);

        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            List<Event> oldEventList = eventRepository.findAllByCompilationId(compilation.getId());
            for (Event oldEvent : oldEventList) {
                oldEvent.setCompilation(null);
            }
            eventRepository.saveAll(oldEventList);

            List<Event> newEventList = eventRepository.findAllById(updateCompilationRequest.getEvents());
            updateCompilationAndEvents(compilation, newEventList);
        }

        return CompilationMapper.mapCompilationToDto(compilation);
    }

    private void checkExistsCompilationById(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new DataNotFoundException("Compilation with id=" + compId + " was not found");
        }
    }

    private Compilation takeCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new DataNotFoundException("Compilation with id=" + compId + " was not found"));
    }

    private void updateCompilationAndEvents(Compilation compilation, List<Event> newEventList) {
        compilation.setEvents(newEventList);
        for (Event newEvent : compilation.getEvents()) {
            newEvent.setCompilation(compilation);
        }
        eventRepository.saveAll(compilation.getEvents());
    }
}