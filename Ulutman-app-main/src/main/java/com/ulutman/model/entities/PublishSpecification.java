package com.ulutman.model.entities;

import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.Metro;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.stream.Collectors;

public class PublishSpecification {

    public static Specification<Publish> hasCategoryIn(List<Category> categories) {
        return (root, query, criteriaBuilder) -> {
            if (categories == null || categories.isEmpty()) {
                return criteriaBuilder.conjunction(); // Всегда true, если categories не заданы
            }
            return root.get("category").in(categories);
        };
    }

    public static Specification<Publish> hasTitleContaining(List<String> titles) {
        return (root, query, criteriaBuilder) -> {
            if (titles == null || titles.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicates = titles.stream()
                    .map(title -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("title")),
                            "%" + title.toLowerCase() + "%"
                    ))
                    .collect(Collectors.toList());

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Publish> hasMetroIn(List<Metro> metros) {
        return (root, query, criteriaBuilder) -> {
            if (metros == null || metros.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("metro").in(metros);
        };
    }
}
