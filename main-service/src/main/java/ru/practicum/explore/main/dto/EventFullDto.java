package ru.practicum.explore.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.explore.main.enums.EventState;

import java.time.LocalDateTime;

@Jacksonized
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private LocationDto location;
    @NotNull
    private Boolean paid;
    @Builder.Default
    private Integer participantLimit = 0;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    @Builder.Default
    private Boolean requestModeration = true;
    private EventState state;
    @NotBlank
    private String title;
    @Builder.Default
    private Long views = 0L;
}