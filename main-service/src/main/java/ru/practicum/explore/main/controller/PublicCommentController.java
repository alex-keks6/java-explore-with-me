package ru.practicum.explore.main.controller;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/events/{eventId}/comments")
public class PublicCommentController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getEventComments(@Positive @PathVariable Long eventId,
                                             @PositiveOrZero
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @Positive
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        return commentService.getEventComments(eventId, from, size);
    }

    @GetMapping("/{commentId}")
    public CommentDto getEventComment(@Positive @PathVariable Long eventId,
                                      @Positive @PathVariable Long commentId) {
        return commentService.getEventComment(eventId, commentId);
    }
}
