package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.CommentDto;
import ru.practicum.explorewithme.dto.NewCommentDto;
import ru.practicum.explorewithme.models.Comment;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toComment(User user, Event event, NewCommentDto newCommentDto) {
        Comment comment = new Comment();
        comment.setCreated(LocalDateTime.now());
        comment.setText(newCommentDto.getText());
        comment.setCommentator(user);
        comment.setEvent(event);
        return comment;
    }

    public static CommentDto toCommentDto(Comment comment) {
        return new CommentDto(comment.getId(),
                comment.getCommentator().getId(),
                comment.getEvent().getId(),
                comment.getText(),
                comment.getCreated());
    }
}
