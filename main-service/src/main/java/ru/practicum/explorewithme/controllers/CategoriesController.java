package ru.practicum.explorewithme.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.dto.NewCategoryDto;
import ru.practicum.explorewithme.services.CategoriesService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Slf4j
@Validated
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryPublic(@Positive @PathVariable Long catId) {
        log.info("Get /categories/{}", catId);
        CategoryDto categoryDto = categoriesService.getCategoryPublic(catId);
        log.info("Return category={}", categoryDto);
        return categoryDto;
    }

    @GetMapping("/categories")
    public List<CategoryDto> findAllCategoriesPublic(@PositiveOrZero @RequestParam(defaultValue = "0", required = false) Integer from,
                                               @Positive @RequestParam(defaultValue = "10", required = false) Integer size) {
        log.info("Get /categories with parameters: from={}, size={}", from, size);
        List<CategoryDto> categoryDtos = categoriesService.findAllCategoriesPublic(PageRequest.of(from / size, size));
        log.info("Return categories={}", categoryDtos);
        return categoryDtos;
    }

    @PatchMapping("/admin/categories")
    public CategoryDto updateCategoryByAdmin(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Patch /admin/categories with body category={}", categoryDto);
        CategoryDto category = categoriesService.updateCategoryByAdmin(categoryDto);
        log.info("Return category={}", category);
        return category;
    }

    @PostMapping("/admin/categories")
    public CategoryDto createCategoryByAdmin(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("Post /admin/categories with body category={}", newCategoryDto);
        CategoryDto categoryDto = categoriesService.createCategoryByAdmin(newCategoryDto);
        log.info("Return category={}", categoryDto);
        return categoryDto;
    }

    @DeleteMapping("/admin/categories/{catId}")
    public void deleteCategoryByAdmin(@PathVariable Long catId) {
        log.info("Delete /admin/categories/{}", catId);
        categoriesService.deleteCategoryByAdmin(catId);
    }
}
