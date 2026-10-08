package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.AdminCommentSearchParameterDto;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.enums.State;
import ru.practicum.explore.main.request.UpdateCommentAdminRequest;
import ru.practicum.explore.main.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/admin/comments")
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getCommentsByAdmin(@RequestParam(required = false) List<@Positive Long> users,
                                               @RequestParam(required = false) List<@Positive Long> events,
                                               @RequestParam(required = false) List<State> states,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               @RequestParam(required = false) LocalDateTime rangeStart,
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                               @RequestParam(required = false) LocalDateTime rangeEnd,
                                               @PositiveOrZero
                                               @RequestParam(required = false, defaultValue = "0") Integer from,
                                               @Positive
                                               @RequestParam(required = false, defaultValue = "10") Integer size) {
        AdminCommentSearchParameterDto searchParameter = new AdminCommentSearchParameterDto(users, events, states,
                rangeStart, rangeEnd, from, size);
        return commentService.getCommentsByAdmin(searchParameter);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateCommentByAdmin(@PathVariable Long commentId,
                                           @Valid @RequestBody UpdateCommentAdminRequest updateCommentAdminRequest) {
        return commentService.updateCommentByAdmin(commentId, updateCommentAdminRequest);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentByAdmin(@Positive @PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(commentId);
    }
}
