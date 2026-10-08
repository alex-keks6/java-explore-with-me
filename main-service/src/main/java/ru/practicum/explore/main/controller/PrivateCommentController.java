package ru.practicum.explore.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.dto.NewCommentDto;
import ru.practicum.explore.main.request.UpdateCommentUserRequest;
import ru.practicum.explore.main.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getOwnCommentsByUser(@Positive @PathVariable Long userId,
                                                 @PositiveOrZero
                                                 @RequestParam(required = false, defaultValue = "0") Integer from,
                                                 @Positive
                                                 @RequestParam(required = false, defaultValue = "10") Integer size) {
        return commentService.getOwnCommentsByUser(userId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getOwnCommentByUser(@Positive @PathVariable Long userId,
                                          @Positive @PathVariable Long commentId) {
        return commentService.getOwnCommentByUser(userId, commentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createCommentByUser(@Positive @PathVariable Long userId,
                                          @Positive @RequestParam Long eventId,
                                          @Valid @RequestBody NewCommentDto newCommentDto) {
        return commentService.createCommentByUser(userId, eventId, newCommentDto);
    }

    @PatchMapping("/{commentId}")
    public CommentDto updateOwnCommentByUser(@Positive @PathVariable Long userId,
                                             @Positive @PathVariable Long commentId,
                                             @Valid @RequestBody UpdateCommentUserRequest updateCommentUserRequest) {
        return commentService.updateOwnCommentByUser(userId, commentId, updateCommentUserRequest);
    }
}
