package ru.practicum.explorewithme.services;

import ru.practicum.explorewithme.dto.CommentDto;
import ru.practicum.explorewithme.dto.NewCommentDto;

import java.util.List;

public interface CommentsService {

    CommentDto createCommentByUser(Long userId, Long eventId, NewCommentDto newCommentDto);

    CommentDto changeCommentByUser(Long userId, Long commentId, NewCommentDto newCommentDto);

    void deleteCommentByUser(Long userId, Long commentId);

    void deleteCommentByAdmin(Long commentId);

    List<CommentDto> findAllCommentsByEventPublic(Long eventId, Integer from, Integer size);
}
