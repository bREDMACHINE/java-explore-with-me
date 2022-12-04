package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
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
import ru.practicum.explorewithme.repositories.EventsRepository;
import ru.practicum.explorewithme.repositories.UsersRepository;
import ru.practicum.explorewithme.services.CommentsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentsServiceImpl implements CommentsService {

    private final CommentsRepository commentsRepository;
    private final UsersRepository usersRepository;
    private final EventsRepository eventsRepository;

    @Override
    public CommentDto createCommentByUser(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User user = usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
        Event event = eventsRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found."));
        Comment comment = CommentMapper.toComment(user, event, newCommentDto);
        return CommentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Override
    public CommentDto changeCommentByUser(Long userId, Long commentId, NewCommentDto newCommentDto) {
        Comment comment = getCommentByIdAndCommentatorId(commentId, userId);
        comment.setText(newCommentDto.getText());
        return CommentMapper.toCommentDto(commentsRepository.save(comment));
    }

    @Override
    public void deleteCommentByUser(Long userId, Long commentId) {
        commentsRepository.delete(getCommentByIdAndCommentatorId(commentId, userId));
    }

    @Override
    public void deleteCommentByAdmin(Long commentId) {
        commentsRepository.delete(commentsRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id=" + commentId + " was not found.")));
    }

    @Override
    public List<CommentDto> findAllCommentsByEventPublic(Long eventId, Integer from, Integer size) {
        return commentsRepository.findAllByEventId(eventId, PageRequest.of(from / size, size)).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
    }

    private Comment getCommentByIdAndCommentatorId(Long commentId, Long userId) {
        return commentsRepository.findByIdAndCommentatorId(commentId, userId)
                .orElseThrow(() -> new BadRequestException("The change is not available"));
    }
}
