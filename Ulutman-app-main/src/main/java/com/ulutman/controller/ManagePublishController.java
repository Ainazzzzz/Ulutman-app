package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.model.dto.PublishDetailsResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.service.ManagePublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/manage/publishes")
@Tag(name = "Manage Publishes")
@SecurityRequirement(name = "Authorization")
public class ManagePublishController {

    private final ManagePublishService managePublicationsService;

    @Operation(summary = "Get all publications")
    @ApiResponse(responseCode = "201", description = "Return list of publishes")
    @GetMapping("/getAll")
    public List<PublishDetailsResponse> getAll() {
        return managePublicationsService.getAllPublish();
    }

    @Operation(summary = "Update a publication status")
    @ApiResponse(responseCode = "201", description = "Updated the publication status by id successfully")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<PublishDetailsResponse> updatePublishStatus(
            @PathVariable Long id,
            @RequestParam PublishStatus newStatus) {
        PublishDetailsResponse response = managePublicationsService.updatePublishStatus(id, newStatus);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter publishes by criteria")
    @ApiResponse(responseCode = "201", description = "Publishes successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<PublishDetailsResponse>> filterPublishes(
            @RequestParam(required = false) List<Category> categories,
            @RequestParam(required = false) List<PublishStatus> publishStatuses,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String names) {

        List<PublishDetailsResponse> filteredPublishes = managePublicationsService.filterPublishes(categories, publishStatuses, createDates, names);

        return ResponseEntity.ok(filteredPublishes);
    }

    @Operation(summary = "Reset filters publications")
    @ApiResponse(responseCode = "201", description = "Publishes filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishDetailsResponse> resetFilter() {
        return managePublicationsService.getAllPublish();
    }

    @Operation(summary = "Delete publishes  by array by id")
    @ApiResponse(responseCode = "201", description = "Deleted, the array of publications is successful")
    @DeleteMapping("/delete/batch")
    public ResponseEntity<String> deletePublicationsByIds(@RequestBody List<Long> ids) {
        try {
            managePublicationsService.deletePublicationsByIds(ids);
            return ResponseEntity.ok("Публикации успешно удалены");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Некоторые публикации   не найдены");
        }
    }

    //last
    @Operation(summary = "Create a activatePublication")
    @ApiResponse(responseCode = "201", description = "activatePublication created successfully")
    @PostMapping("/activate/{publicationId}")
    public ResponseEntity<String> activatePublication(@PathVariable Long publicationId) {
        managePublicationsService.activatePublish(publicationId);
        return ResponseEntity.ok("Публикация активирована успешно.");
    }
}
