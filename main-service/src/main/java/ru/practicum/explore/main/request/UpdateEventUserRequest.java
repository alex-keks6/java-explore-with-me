package ru.practicum.explore.main.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.explore.main.enums.EventReview;

@Jacksonized
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest extends UpdateEventRequest {
    private EventReview stateAction;
}