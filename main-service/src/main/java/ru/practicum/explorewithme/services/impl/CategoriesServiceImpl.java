package ru.practicum.explorewithme.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.dto.CategoryDto;
import ru.practicum.explorewithme.dto.NewCategoryDto;
import ru.practicum.explorewithme.exceptions.NotFoundException;
import ru.practicum.explorewithme.mappers.CategoryMapper;
import ru.practicum.explorewithme.models.Category;
import ru.practicum.explorewithme.repositories.CategoriesRepository;
import ru.practicum.explorewithme.services.CategoriesService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;

    @Transactional(readOnly = true)
    @Override
    public CategoryDto getCategory(Long catId) {
        return CategoryMapper.toCategoryDto(getCategoryForServices(catId));
    }


    @Override
    public Category getCategoryForServices(Long catId) {
        return categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found."));
    }

    @Transactional(readOnly = true)
    @Override
    public List<CategoryDto> findAllCategories(Pageable pageable) {
        return categoriesRepository.findAll(pageable).stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CategoryDto updateCategoryByAdmin(CategoryDto categoryDto) {
        Category category = getCategoryForServices(categoryDto.getId());
            category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(categoriesRepository.save(category));
    }


    @Transactional
    @Override
    public CategoryDto createCategoryByAdmin(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        return CategoryMapper.toCategoryDto(categoriesRepository.save(category));
    }

    @Transactional
    @Override
    public void deleteCategoryByAdmin(Long catId) {
        Category category = getCategoryForServices(catId);
        categoriesRepository.delete(category);
    }
}
