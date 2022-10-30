package ru.practicum.explorewithme.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.*;
import ru.practicum.explorewithme.services.CategoriesService;
import ru.practicum.explorewithme.services.CompilationsService;
import ru.practicum.explorewithme.services.EventsService;
import ru.practicum.explorewithme.services.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class AdminController {

    private final CategoriesService categoriesService;
    private final EventsService eventsService;
    private final CompilationsService compilationsService;
    private final UserService userService;

    @GetMapping("/events")
    public List<EventFullDto> findAllEventsByAdmin(@RequestParam List<Integer> users,
                                                     @RequestParam List<String> states,
                                                     @RequestParam List<Integer> categories,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                     @RequestParam LocalDateTime rangeStart,
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                     @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                     @RequestParam LocalDateTime rangeEnd,
                                                     @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get all events with parameters: users={}, " +
                "states={}, " +
                "categories={}, " +
                "rangeStart={}, " +
                "rangeEnd={}, " +
                "from={}, " +
                "size={},", users, states, categories, rangeStart, rangeEnd, from, size);
        return eventsService.findAllEventsByAdmin(users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                PageRequest.of(from / size, size));
    }

    @PutMapping("/events/{eventId}")
    public EventFullDto updateEventByAdmin(@Positive @PathVariable Long eventId,
                                             @RequestBody AdminUpdateEventRequest adminUpdateEventRequest) {
        log.info("Put event id={}, updateEvent={},", eventId, adminUpdateEventRequest);
        return eventsService.updateEventByAdmin(eventId, adminUpdateEventRequest);
    }

    @PatchMapping("/events/{eventId}/publish")
    public EventFullDto publishEventByAdmin(@Positive @PathVariable Long eventId) {
        log.info("Patch publish event id={}", eventId);
        return eventsService.publishEventByAdmin(eventId);
    }

    @PatchMapping("/events/{eventId}/reject")
    public EventFullDto rejectEventByAdmin(@Positive @PathVariable Long eventId) {
        log.info("Patch reject event id={}", eventId);
        return eventsService.rejectEventByAdmin(eventId);
    }

    @PatchMapping("/categories")
    public CategoryDto updateCategoryByAdmin(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Patch category={}", categoryDto);
        return categoriesService.updateCategoryByAdmin(categoryDto);
    }

    @PostMapping("/categories")
    public CategoryDto createCategoryByAdmin(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Post category={}", newCategoryDto);
        return categoriesService.createCategoryByAdmin(newCategoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    public void deleteCategoryByAdmin(@PathVariable Long catId) {
        log.info("Delete categoryId={}", catId);
        categoriesService.deleteCategoryByAdmin(catId);
    }

    @GetMapping("/users")
    public List<UserDto> findAllFullUsersByAdmin(@RequestParam List<Long> ids,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get all users with parameters: users={}, from={}, size={}", ids, from, size);
        return userService.findAllUsersByAdmin(ids, PageRequest.of(from / size, size));
    }

    @PostMapping("/users")
    public UserDto createUserByAdmin(@Valid @RequestBody NewUserRequest newUserRequest) {
        log.info("Post user={}", newUserRequest);
        return userService.createUserByAdmin(newUserRequest);
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUserByAdmin(@Positive @PathVariable Long userId) {
        log.info("Delete userId={}", userId);
        userService.deleteUserByAdmin(userId);
    }

    @PostMapping("/compilations")
    public CompilationDto createCompilationsByAdmin(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post compilation={}", newCompilationDto);
        return compilationsService.createCompilationsByAdmin(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    public void deleteCompilationsByAdmin(@Positive @PathVariable Long compId) {
        log.info("Delete compilationId={}", compId);
        compilationsService.deleteCompilationsByAdmin(compId);
    }

    @DeleteMapping("/compilations/{compId}/events/{eventId}")
    public void deleteEventFromCompilationsByAdmin(@Positive @PathVariable Long compId,
                                                   @Positive @PathVariable Long eventId) {
        log.info("Delete event from compilationId={}, eventId={}", compId, eventId);
        compilationsService.deleteEventInCompilationsByAdmin(compId, eventId);
    }

    @PatchMapping("/compilations/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilationsByAdmin(@Positive @PathVariable Long compId,
                                                        @Positive @PathVariable Long eventId) {
        log.info("Patch event to compilationId={}, eventId={}", compId, eventId);
        return compilationsService.addEventToCompilationsByAdmin(compId, eventId);
    }

    @DeleteMapping("/compilations/{compId}/pin")
    public void deleteFromWallCompilationsByAdmin(@Positive @PathVariable Long compId) {
        log.info("Delete pin compilationId={}", compId);
        compilationsService.deleteFromWallCompilationsByAdmin(compId);
    }

    @PatchMapping("/compilations/{compId}/pin")
    public void pinOnWallCompilationsByAdmin(@Positive @PathVariable Long compId) {
        log.info("Patch pin compilationId={}", compId);
        compilationsService.pinOnWallCompilationsByAdmin(compId);
    }
}
