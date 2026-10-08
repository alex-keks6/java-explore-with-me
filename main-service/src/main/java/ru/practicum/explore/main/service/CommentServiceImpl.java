package ru.practicum.explore.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.explore.main.dto.AdminCommentSearchParameterDto;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.dto.NewCommentDto;
import ru.practicum.explore.main.enums.State;
import ru.practicum.explore.main.exception.DataNotFoundException;
import ru.practicum.explore.main.exception.DataValidationException;
import ru.practicum.explore.main.mapper.CommentMapper;
import ru.practicum.explore.main.model.Comment;
import ru.practicum.explore.main.model.Event;
import ru.practicum.explore.main.model.User;
import ru.practicum.explore.main.repository.CommentRepository;
import ru.practicum.explore.main.repository.EventRepository;
import ru.practicum.explore.main.request.UpdateCommentAdminRequest;
import ru.practicum.explore.main.request.UpdateCommentRequest;
import ru.practicum.explore.main.request.UpdateCommentUserRequest;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final UserService userService;
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CommentDto> getOwnCommentsByUser(Long userId, Integer from, Integer size) {
        userService.checkExistsUserById(userId);

        checkCorrectPageParameters(from, size);

        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        List<Comment> commentList = commentRepository.findAllByCommenterIdOrderByCreatedOnDesc(userId, page).getContent();

        return commentList.stream()
                .map(CommentMapper::mapCommentToDto)
                .toList();
    }

    @Override
    public CommentDto getOwnCommentByUser(Long userId, Long commentId) {
        userService.checkExistsUserById(userId);

        Comment comment = takeCommentById(commentId);

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
        Comment comment = takeCommentById(commentId);

        updateCommentByRequest(comment, updateCommentUserRequest);
        if (updateCommentUserRequest.getStateAction() != null) {
            switch (updateCommentUserRequest.getStateAction()) {
                case SEND_TO_REVIEW:
                    comment.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    comment.setState(State.CANCELED);
                    break;
            }
        }

        return CommentMapper.mapCommentToDto(commentRepository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByAdmin(AdminCommentSearchParameterDto searchParameter) {
        checkCorrectPageParameters(searchParameter.getFrom(), searchParameter.getSize());

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

        updateCommentByRequest(comment, updateCommentAdminRequest);
        if (updateCommentAdminRequest.getStateAction() != null) {
            switch (updateCommentAdminRequest.getStateAction()) {
                case PUBLISH_EVENT:
                    comment.setState(State.PUBLISHED);
                    comment.setPublishedOn(LocalDateTime.now());
                    break;
                case REJECT_EVENT:
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

    private void checkCorrectPageParameters(Integer from, Integer size) {
        if (from % size != 0) {
            throw new DataValidationException("from=" + from + " and size=" + size + " is incorrect");
        }
    }

    private void updateCommentByRequest(Comment comment, UpdateCommentRequest updateCommentRequest) {
        if (updateCommentRequest.getText() != null) {
            comment.setText(updateCommentRequest.getText());
        }
    }

    private void checkExistsCommentById(Long commentId) {
        if (!commentRepository.existsById(commentId)) {
            throw new DataNotFoundException("Comment with id=" + commentId + " was not found");
        }
    }
}
