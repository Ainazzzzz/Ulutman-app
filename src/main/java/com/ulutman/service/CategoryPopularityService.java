package com.ulutman.service;

import com.ulutman.model.entities.CategoryPopularity;
import com.ulutman.model.enums.Category;
import com.ulutman.repository.CategoryPopularityRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryPopularityService {

    private final CategoryPopularityRepository popularityRepository;

    public void incrementViews(Category category) {
        CategoryPopularity popularity = popularityRepository.findByCategory(category)
                .orElse(new CategoryPopularity());
        popularity.setCategory(category);
        popularity.setViews(popularity.getViews() + 1);
        popularityRepository.save(popularity);
    }

    public Map<Category, Integer> getCategoryPopularity(boolean byViews) {
        Map<Category, Integer> popularityMap = new HashMap<>();
        List<CategoryPopularity> popularities = popularityRepository.findAll();

        for (CategoryPopularity popularity : popularities) {
            int popularityCount = byViews ? popularity.getViews() : popularity.getPublications();
            popularityMap.put(popularity.getCategory(), popularityCount);
        }

        return popularityMap;
    }
}
