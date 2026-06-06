package ru.practicum.shareit.item.comment.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.comment.Comment;

import java.util.Collection;

@UtilityClass
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getItem().getId(),
                comment.getAuthor().getId(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Collection<CommentDto> toCommentDtoCollection(Collection<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDto).toList();
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(commentDto.getText());
    }
}
