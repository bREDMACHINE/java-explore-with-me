package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CommentDto;
import ru.practicum.explorewithme.dto.NewCommentDto;
import ru.practicum.explorewithme.services.CommentsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class CommentsController {

    private final CommentsService commentService;

    @GetMapping("/event/{eventId}/comments")
    public List<CommentDto> findAllCommentsByEventPublic(
            @PathVariable Long eventId,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        log.info("Get /event/{}/comments with parameters from={}, size={}", eventId, from, size);
        List<CommentDto> commentDtos = commentService.findAllCommentsByEventPublic(eventId, from, size);
        log.info("Return comments={}", commentDtos);
        return commentDtos;
    }

    @PostMapping("/users/{userId}/event/{eventId}/comment")
    public CommentDto createCommentByUser(@PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Post /users/{}/event/{}/comment with body comment={}", userId, eventId, newCommentDto);
        CommentDto commentDto = commentService.createCommentByUser(userId, eventId, newCommentDto);
        log.info("Return comment={}", commentDto);
        return commentDto;
    }

    @PatchMapping("/users/{userId}/comment/{commentId}")
    public CommentDto changeCommentByUser(@PathVariable Long userId,
                                          @PathVariable Long commentId,
                                          @Valid @RequestBody NewCommentDto newCommentDto) {
        log.info("Patch /users/{}/comment/{} with body comment={}", userId, commentId, newCommentDto);
        CommentDto commentDto = commentService.changeCommentByUser(userId, commentId, newCommentDto);
        log.info("Return comment={}", commentDto);
        return commentDto;
    }

    @DeleteMapping("/users/{userId}/comment/{commentId}")
    public void deleteCommentByUser(@PathVariable Long userId,
                                          @PathVariable Long commentId) {
        log.info("Delete /users/{}/comment/{}", userId, commentId);
        commentService.deleteCommentByUser(userId, commentId);
    }

    @DeleteMapping("/admin/comment/{commentId}")
    public void deleteCommentByAdmin(@PathVariable Long commentId) {
        log.info("Delete /admin/comment/{}", commentId);
        commentService.deleteCommentByAdmin(commentId);
    }
}
