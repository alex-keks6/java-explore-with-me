package ru.practicum.explore.main.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.explore.main.enums.UserCommentUpdateState;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentUserRequest {
    @Size(min = 1, max = 10000)
    private String text;
    private UserCommentUpdateState stateAction;
}
