package ru.practicum.explorewithme.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.User;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {

    List<User> findUsersByIdIn(List<Long> ids, Pageable pageable);
}
