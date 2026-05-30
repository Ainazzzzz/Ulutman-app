package com.ulutman.repository;

import com.ulutman.model.entities.CategoryPopularity;
import com.ulutman.model.enums.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryPopularityRepository extends JpaRepository<CategoryPopularity, Long> {

    Optional<CategoryPopularity> findByCategory(Category category);

}
