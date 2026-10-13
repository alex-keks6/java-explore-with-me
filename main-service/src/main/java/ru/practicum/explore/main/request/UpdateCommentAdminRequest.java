package ru.practicum.explore.main.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.practicum.explore.main.enums.AdminCommentUpdateState;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentAdminRequest {
    private AdminCommentUpdateState stateAction;
}
