package com.ulutman.controller;

import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.FilteredPublishResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.service.ManageCategoryService;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/manage/category")
@Tag(name = "Manage Category")
@SecurityRequirement(name = "Authorization")
public class ManageCategoryController {

    private final ManageCategoryService manageCategoryService;
    private final PublishService publishService;

    @Operation(summary = "Get all publications")
    @ApiResponse(responseCode = "201", description = "Return list of publishes")
    @GetMapping("/getAll")
    public List<FilteredPublishResponse> getAll() {
        return manageCategoryService.getAllCategory();
    }

    @Operation(summary = "Get all users with publications")
    @ApiResponse(responseCode = "201", description = "Return the list of the user's publications")
    @GetMapping("/with-publishes")
    public ResponseEntity<List<AuthResponse>> getAllUsersWithPublishes() {
        List<AuthResponse> users = manageCategoryService.getAllUsersWithPublishes();

        return ResponseEntity.ok(users);
    }

    @Operation(summary = "Update user status")
    @ApiResponse(responseCode = "201", description = "Updated category status successfully")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<FilteredPublishResponse> updateCategoryStatus(
            @PathVariable Long id,
            @RequestParam CategoryStatus newStatus) {

        FilteredPublishResponse filteredPublishResponse = manageCategoryService.updateCategoryStatus(id, newStatus);
        return ResponseEntity.ok(filteredPublishResponse);
    }

    @Operation(summary = "Filter categories by criteria")
    @ApiResponse(responseCode = "201", description = "Categories successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<FilteredPublishResponse>> filterPublishesCriteria(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) List<Category> categories,
            @RequestParam(required = false) List<CategoryStatus> categoryStatuses,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String names) {

        List<FilteredPublishResponse> filteredPublishes = manageCategoryService.filterPublishes(
                categories, categoryStatuses, createDates, title, names);

        return ResponseEntity.ok(filteredPublishes);
    }

    @Operation(summary = "Reset filters categories")
    @ApiResponse(responseCode = "201", description = "Category filters successfully reset")
    @GetMapping("/resetFilter")
    public List<FilteredPublishResponse> resetFilter() {
        return manageCategoryService.getAllCategory();
    }

    @Operation(summary = "Count user publications ")
    @ApiResponse(responseCode = "201", description = "Return count of the user's publications")
    @GetMapping("/count/{userId}")
    public int getNumberOfPublications(@PathVariable Long userId) {
        return publishService.getNumberOfPublications(userId);
    }
}
