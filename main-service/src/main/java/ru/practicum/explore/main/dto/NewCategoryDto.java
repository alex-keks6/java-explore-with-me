package ru.practicum.explore.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
