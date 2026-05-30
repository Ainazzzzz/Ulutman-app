package com.ulutman.controller;

import com.ulutman.model.dto.PublishResponse;
import com.ulutman.service.ManageCreatePaymentSystem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class ManageCreatePaymentSystemController {
    private final ManageCreatePaymentSystem manageCreatePaymentSystem;

    @Operation(summary = "Create a getAllDeactivatedPublications")
    @ApiResponse(responseCode = "201", description = "getAllDeactivatedPublications created successfully")
    @GetMapping("/deactivated")
    public ResponseEntity<List<PublishResponse>> findDeactivatedPublications() {
        try {
            List<PublishResponse> deactivatedPublications = manageCreatePaymentSystem.findAllDeactivatedPublications();
            return ResponseEntity.ok(deactivatedPublications);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Create a activatePublication")
    @ApiResponse(responseCode = "201", description = "activatePublication created successfully")
    @PostMapping("/activate/{publicationId}")
    public ResponseEntity<String> activatePublication(@PathVariable Long publicationId) {
        manageCreatePaymentSystem.activatePublication(publicationId);
        return ResponseEntity.ok("Публикация активирована успешно.");
    }

    @Operation(summary = "Create a deactivatePublication")
    @ApiResponse(responseCode = "201", description = "deactivatePublication created successfully")
    @PostMapping("/deactivate/{publicationId}")
    public ResponseEntity<String> deactivatePublication(@PathVariable Long publicationId) {
        manageCreatePaymentSystem.deactivatePublication(publicationId);
        return ResponseEntity.ok("Публикация деактивирована успешно.");
    }
}
