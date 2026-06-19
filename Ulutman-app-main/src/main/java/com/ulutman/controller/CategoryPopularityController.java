package com.ulutman.controller;

import com.ulutman.model.enums.Category;
import com.ulutman.service.CategoryPopularityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard/categories")
@RequiredArgsConstructor
@Tag(name = "Category popularity")
@SecurityRequirement(name = "Authorization")
public class CategoryPopularityController {

    private final CategoryPopularityService popularityService;

    @Operation(summary = "View categories")
    @ApiResponse(responseCode = "201", description = "Viewed category successfully")
    @PostMapping("/view/{category}")  //просмотр категорий
    public ResponseEntity<String> viewCategory(@PathVariable Category category) {
        popularityService.incrementViews(category);
        return ResponseEntity.ok("Просмотр увеличен для каждой категории: " + category);
    }

    @Operation(summary = "Get to view categories")
    @ApiResponse(responseCode = "201", description = "Received viewed categories successfully")
    @GetMapping("/popularity/views")
    public ResponseEntity<Map<Category, Integer>> getPopularityByViews() {
        Map<Category, Integer> popularity = popularityService.getCategoryPopularity(true);
        return ResponseEntity.ok(popularity);
    }

    @Operation(summary = "Get to popularity publications")
    @ApiResponse(responseCode = "201", description = "Received popularity publications successfully")
    @GetMapping("/popularity/publications")
    public ResponseEntity<Map<Category, Integer>> getPopularityByPublications() {
        Map<Category, Integer> popularity = popularityService.getCategoryPopularity(false);
        return ResponseEntity.ok(popularity);
    }
}
