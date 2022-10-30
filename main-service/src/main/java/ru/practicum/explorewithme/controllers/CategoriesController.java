package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.services.CategoriesService;
import ru.practicum.explorewithme.dto.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@Positive @PathVariable Long catId) {
        log.info("Get categories id={}", catId);
        return categoriesService.getCategory(catId);
    }

    @GetMapping
    public List<CategoryDto> findAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                               @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("Find all categories with parameters: from={}, size={}", from, size);
        return categoriesService.findAllCategories(PageRequest.of(from / size, size));
    }
}
