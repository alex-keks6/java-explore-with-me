package ru.practicum.explore.main.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ru.practicum.explore.main.enums.UserUpdateState;

@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentUserRequest extends UpdateCommentRequest {
    private UserUpdateState stateAction;
}
