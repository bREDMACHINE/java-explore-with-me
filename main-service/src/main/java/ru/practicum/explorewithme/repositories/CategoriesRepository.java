package ru.practicum.explorewithme.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.models.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
