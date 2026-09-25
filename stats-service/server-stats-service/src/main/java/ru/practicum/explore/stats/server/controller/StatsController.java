package ru.practicum.explore.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explore.stats.dto.StatsDto;
import ru.practicum.explore.stats.server.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<StatsDto> getStats(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam LocalDateTime start,
                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") @RequestParam LocalDateTime end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        return statsService.getStats(start, end, uris, unique);
    }
}
