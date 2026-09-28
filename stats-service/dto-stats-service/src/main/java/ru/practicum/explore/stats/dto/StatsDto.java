package ru.practicum.explore.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class StatsDto {
    private String app;
    private String uri;
    private Long hits;
}
