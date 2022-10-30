package ru.practicum.explorewithme.mappers;

import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.dto.NewCategoryDto;
import ru.practicum.explorewithme.models.Category;

public class CategoryMapper {

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        Category category = new  Category();
        category.setName(newCategoryDto.getName());
        return category;
    }
}
