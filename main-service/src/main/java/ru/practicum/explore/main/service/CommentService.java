package ru.practicum.explore.main.service;

import ru.practicum.explore.main.dto.AdminCommentSearchParameterDto;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.dto.NewCommentDto;
import ru.practicum.explore.main.model.Comment;
import ru.practicum.explore.main.request.UpdateCommentAdminRequest;
import ru.practicum.explore.main.request.UpdateCommentUserRequest;

import java.util.List;

public interface CommentService {
    List<CommentDto> getOwnCommentsByUser(Long userId, Integer from, Integer size);

    CommentDto getOwnCommentByUser(Long userId, Long commentId);

    CommentDto createCommentByUser(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto updateOwnCommentByUser(Long userId, Long commentId, UpdateCommentUserRequest updateCommentUserRequest);

    List<CommentDto> getCommentsByAdmin(AdminCommentSearchParameterDto searchParameter);

    CommentDto updateCommentByAdmin(Long commentId, UpdateCommentAdminRequest updateCommentAdminRequest);

    void deleteCommentByAdmin(Long commentId);

    Comment takeCommentById(Long commentId);
}
