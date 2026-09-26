package ru.practicum.explore.stats.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StatsDto {
    private String app;
    private String uri;
    private Integer hits;
}
