package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.NewUserRequest;
import ru.practicum.explorewithme.dto.UserDto;
import ru.practicum.explorewithme.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class UsersController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserDto> findAllUsersByAdmin(
            @RequestParam List<Long> ids,
            @PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
            @Positive @RequestParam(defaultValue = "10", required = false) Integer size
    ) {
        log.info("Get /admin/users with parameters: users={}, from={}, size={}", ids, from, size);
        List<UserDto> userDtos = userService.findAllUsersByAdmin(ids, PageRequest.of(from / size, size));
        log.info("Return users={}", userDtos);
        return userDtos;
    }

    @PostMapping("/users")
    public UserDto createUserByAdmin(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Post /admin/users with body user={}", newUserRequest);
        UserDto userDto = userService.createUserByAdmin(newUserRequest);
        log.info("Return user={}", userDto);
        return userDto;
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserByAdmin(@Positive @PathVariable Long userId) {
        log.info("Delete /admin/users/{}", userId);
        userService.deleteUserByAdmin(userId);
    }
}
