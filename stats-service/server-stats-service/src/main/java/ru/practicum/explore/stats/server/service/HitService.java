package ru.practicum.explore.stats.server.service;

import jakarta.servlet.http.HttpServletRequest;

public interface HitService {
    void saveHit(HttpServletRequest request);
}