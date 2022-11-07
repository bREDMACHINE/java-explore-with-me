package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.CommentDto;
import ru.practicum.explorewithme.dto.NewCommentDto;
import ru.practicum.explorewithme.exceptions.BadRequestException;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.CommentMapper;
import ru.practicum.explorewithme.models.Comment;
import ru.practicum.explorewithme.models.Event;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.repositories.CommentsRepository;
import ru.practicum.explorewithme.services.CommentsService;
import ru.practicum.explorewithme.services.EventsService;
import ru.practicum.explorewithme.services.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final UserService userService;
    private final EventsService eventsService;

    @Override
    public CommentDto createCommentByUser(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = userService.getUserForServices(userId);
        Event event = eventsService.getEventForServices(eventId);
        Comment comment = CommentMapper.toComment(user, event, newCommentDto);
        return CommentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Override
    public CommentDto changeCommentByUser(Long userId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = getComment(commentId);
        if (Objects.equals(userService.getUserForServices(userId).getId(), comment.getCommentator().getId())) {
            comment.setText(newCommentDto.getText());
            return CommentMapper.toCommentDto(commentsRepository.save(comment));
        }
        throw new BadRequestException("The change is not available");
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        Comment comment = getComment(commentId);
        if (Objects.equals(userService.getUserForServices(userId).getId(), comment.getCommentator().getId())) {
            commentsRepository.delete(comment);
        }
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        commentsRepository.delete(getComment(commentId));
    }

    @Override
    public List<CommentDto> findAllCommentsByEventPublic(Long eventId, Pageable pageable) {
        return commentsRepository.findAllByEventId(eventId, pageable).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private Comment getComment(Long id) {
        return commentsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + id + " was not found."));
    }
}
