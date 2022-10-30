package ru.practicum.explorewithme.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.models.User;

import java.util.List;

public interface UserService {

    List<UserDto> findAllUsersByAdmin(List<Long> ids, Pageable pageable);

    UserDto createUserByAdmin(NewUserRequest newUserRequest);

    User getUserForServices(Long userId);

    void deleteUserByAdmin(Long userId);
}
