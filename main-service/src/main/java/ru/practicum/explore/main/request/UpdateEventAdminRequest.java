package ru.practicum.explore.main.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import ru.practicum.explore.main.enums.EventStateUpdate;

@Jacksonized
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventAdminRequest extends UpdateEventRequest {
    private EventStateUpdate stateAction;
}