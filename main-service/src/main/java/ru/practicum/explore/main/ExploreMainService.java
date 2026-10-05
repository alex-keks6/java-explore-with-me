package ru.practicum.explore.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "ru.practicum.explore.main",
        "ru.practicum.explore.stats.client",
        "ru.practicum.explore.stats.dto"
})
public class ExploreMainService {
    public static void main(String[] args) {
        SpringApplication.run(ExploreMainService.class, args);
    }
}