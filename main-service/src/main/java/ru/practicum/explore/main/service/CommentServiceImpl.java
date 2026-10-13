package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.AdminCommentSearchParameterDto;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.dto.NewCommentDto;
import ru.practicum.explore.main.enums.AdminCommentUpdateState;
import ru.practicum.explore.main.enums.State;
import ru.practicum.explore.main.exception.DataBadRequestException;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.exception.DataValidationException;
import ru.practicum.explore.main.mapper.CommentMapper;
import ru.practicum.explore.main.model.Comment;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.model.User;
import ru.practicum.explore.main.repository.CommentRepository;
import ru.practicum.explore.main.repository.EventRepository;
import ru.practicum.explore.main.request.UpdateCommentAdminRequest;
import ru.practicum.explore.main.request.UpdateCommentUserRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final EventService eventService;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getEventComments(Long eventId, Integer from, Integer size) {
        Event event = eventService.takeEventById(eventId);

        eventService.checkEventIsPublished(event);

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);

        List<Comment> commentList = commentRepository.findAllByEventIdAndStateOrderByPublishedOnDesc(eventId,
                State.PUBLISHED, page).getContent();

        return commentList.stream()
                .map(CommentMapper::mapCommentToDto)
                .toList();
    }

    @Override
    public CommentDto getEventComment(Long eventId, Long commentId) {
        Event event = eventService.takeEventById(eventId);

        eventService.checkEventIsPublished(event);

        Comment comment = takeCommentByEventIdAndId(eventId, commentId);
        if (comment.getState() != State.PUBLISHED) {
            throw new DataNotFoundException("Comment must be published");
        }

        return CommentMapper.mapCommentToDto(comment);
    }

    @Override
    public List<CommentDto> getOwnCommentsByUser(Long userId, Integer from, Integer size) {
        userService.checkExistsUserById(userId);

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Comment> commentList = commentRepository.findAllByCommenterIdOrderByCreatedOnDesc(userId, page).getContent();

        return commentList.stream()
                .map(CommentMapper::mapCommentToDto)
                .toList();
    }

    @Override
    public CommentDto getOwnCommentByUser(Long userId, Long commentId) {
        userService.checkExistsUserById(userId);

        Comment comment = takeCommentByUserIdAndId(userId, commentId);

        return CommentMapper.mapCommentToDto(comment);
    }

    @Override
    public CommentDto createCommentByUser(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userService.takeUserById(userId);
        Event event = eventRepository.findById(eventId).orElseThrow(() ->
                new DataNotFoundException("Event with id=" + eventId + " was not found"));

        if (event.getState() != State.PUBLISHED) {
            throw new DataValidationException("Only for published event can write comments");
        }

        Comment comment = CommentMapper.mapNewDtoToComment(newCommentDto);
        comment.setCommenter(user);
        comment.setEvent(event);
        comment.setCreatedOn(LocalDateTime.now());
        comment.setState(State.PENDING);

        return CommentMapper.mapCommentToDto(commentRepository.save(comment));
    }

    @Override
    public CommentDto updateOwnCommentByUser(Long userId, Long commentId,
                                             UpdateCommentUserRequest updateCommentUserRequest) {
        userService.checkExistsUserById(userId);

        if (updateCommentUserRequest.getText() != null && updateCommentUserRequest.getText().isBlank()) {
            throw new DataBadRequestException("Update text must contain minimum 1 non‑space character.");
        }

        Comment comment = takeCommentByUserIdAndId(userId, commentId);

        Boolean isCommentUpdate = false;

        if (updateCommentUserRequest.getText() != null) {
            comment.setText(updateCommentUserRequest.getText());
            isCommentUpdate = true;
        }

        if (updateCommentUserRequest.getStateAction() != null) {
            switch (updateCommentUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    comment.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    comment.setState(State.CANCELED);
                    break;
            }
        } else if (isCommentUpdate) {
            comment.setState(State.PENDING);
        }

        return CommentMapper.mapCommentToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        checkExistsCommentByUserIdAndId(userId, commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getCommentsByAdmin(AdminCommentSearchParameterDto searchParameter) {

        PageRequest page = PageRequest.of(searchParameter.getFrom() > 0 ?
                searchParameter.getFrom() / searchParameter.getSize() : 0, searchParameter.getSize());
        List<Comment> commentList = commentRepository.findAllByAdminFilters(searchParameter.getUsers(),
                searchParameter.getEvents(), searchParameter.getStates(), searchParameter.getRangeStart(),
                searchParameter.getRangeEnd(), page).getContent();

        return commentList.stream()
                .map(CommentMapper::mapCommentToDto)
                .toList();
    }

    @Override
    public CommentDto updateCommentByAdmin(Long commentId, UpdateCommentAdminRequest updateCommentAdminRequest) {
        Comment comment = takeCommentById(commentId);

        if (comment.getState() != State.PENDING
                && updateCommentAdminRequest.getStateAction() == AdminCommentUpdateState.PUBLISH_COMMENT) {
            throw new DataValidationException("Cannot publish the comment because it's not in the right state: " +
                    comment.getState());
        }

        if (comment.getState() == State.PUBLISHED
                && updateCommentAdminRequest.getStateAction() == AdminCommentUpdateState.REJECT_COMMENT) {
            throw new DataValidationException("Cannot reject the comment because it's not in the right state: " +
                    comment.getState());
        }

        if (updateCommentAdminRequest.getStateAction() != null) {
            switch (updateCommentAdminRequest.getStateAction()) {
                case PUBLISH_COMMENT:
                    comment.setState(State.PUBLISHED);
                    comment.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_COMMENT:
                    comment.setState(State.CANCELED);
                    break;
            }
        }

        return CommentMapper.mapCommentToDto(commentRepository.save(comment));
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        checkExistsCommentById(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public Comment takeCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new DataNotFoundException("Comment with id=" + commentId + " was not found"));
    }

    private Comment takeCommentByUserIdAndId(Long userId, Long commentId) {
        return commentRepository.findByCommenterIdAndId(userId, commentId).orElseThrow(() ->
                new DataNotFoundException("Comment with id=" + commentId + " and userId=" + userId + " was not found"));
    }

    private Comment takeCommentByEventIdAndId(Long eventId, Long commentId) {
        return commentRepository.findByEventIdAndId(eventId, commentId).orElseThrow(() ->
                new DataNotFoundException("Comment with id=" + commentId + " and eventId=" + eventId + " was not found"));
    }

    private void checkExistsCommentByUserIdAndId(Long userId, Long commentId) {
        if (!commentRepository.existsByCommenterIdAndId(userId, commentId)) {
            throw new DataNotFoundException("Comment with id=" + commentId + " and userId=" + userId + " was not found");
        }
    }

    private void checkExistsCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new DataNotFoundException("Comment with id=" + commentId + " was not found");
        }
    }
}
