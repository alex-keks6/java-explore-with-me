package ru.practicum.explore.main.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.main.dto.CommentDto;
import ru.practicum.explore.main.dto.NewCommentDto;
import ru.practicum.explore.main.model.Comment;

@UtilityClass
public class CommentMapper {
    public CommentDto mapCommentToDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .commenter(UserMapper.mapUserToShortDto(comment.getCommenter()))
                .event(EventMapper.mapEventToShortDto(comment.getEvent()))
                .createdOn(comment.getCreatedOn())
                .state(comment.getState())
                .publishedOn(comment.getPublishedOn())
                .build();
    }

    public Comment mapNewDtoToComment(NewCommentDto newCommentDto) {
        return Comment.builder()
                .text(newCommentDto.getText())
                .build();
    }
}
