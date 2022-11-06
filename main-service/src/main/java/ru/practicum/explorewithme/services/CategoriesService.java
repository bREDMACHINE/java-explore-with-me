package ru.practicum.explorewithme.services;

import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.dto.NewCategoryDto;
import ru.practicum.explorewithme.models.Category;

import java.util.List;

public interface CategoriesService {
    CategoryDto getCategoryPublic(Long catId);

    Category getCategoryForServices(Long catId);

    List<CategoryDto> findAllCategoriesPublic(Pageable pageable);

    CategoryDto updateCategoryByAdmin(CategoryDto categoryDto);

    CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto);

    void deleteCategoryByAdmin(Long catId);
}
