package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.UserMapper;
import ru.practicum.explorewithme.models.User;
import ru.practicum.explorewithme.repositories.UsersRepository;
import ru.practicum.explorewithme.services.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UsersServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAllUsersByAdmin(List<Long> ids, Pageable pageable) {
        return usersRepository.findUsersByIdIn(ids, pageable).stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public User getUserForServices(Long userId) {
        return usersRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found."));
    }

    @Transactional
    @Override
    public UserDto createUserByAdmin(NewUserRequest newUserRequest) {
        User user = UserMapper.toUser(newUserRequest);
        return UserMapper.toUserDto(usersRepository.save(user));
    }

    @Transactional
    @Override
    public void deleteUserByAdmin(Long userId) {
        User user = getUserForServices(userId);
        usersRepository.delete(user);
    }
}
