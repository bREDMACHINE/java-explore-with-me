package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentsRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long eventId, Pageable pageable);

    Optional<Comment> findByIdAndCommentatorId(Long commentId, Long userId);
}
