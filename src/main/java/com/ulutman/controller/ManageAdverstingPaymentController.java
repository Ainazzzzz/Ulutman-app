package com.ulutman.controller;

import com.ulutman.model.dto.AdVersitingResponse;
import com.ulutman.model.entities.AdVersiting;
import com.ulutman.service.ManagePaymentAdversting;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manage/adversting")
@Tag(name = "Manage AdVersting")
@SecurityRequirement(name = "Authorization")
public class ManageAdverstingPaymentController {

    private final ManagePaymentAdversting managePaymentAdversting;

    @GetMapping("/deactivated")
    @Operation(summary = "Create a getAllDeactivatedPublications")
    @ApiResponse(responseCode = "200", description = "getAllDeactivatedPublications retrieved successfully")
    public ResponseEntity<List<AdVersitingResponse>> getAllDeactivatedPublications() {
        List<AdVersiting> deactivatedPublications = managePaymentAdversting.getAllDeactivatedPublications();

        List<AdVersitingResponse> responseList = deactivatedPublications.stream()
                .map(ad -> AdVersitingResponse.builder()
                        .id(ad.getId())
                        .imageFile(ad.getImagePath())
                        .active(ad.isActive())
                        .paymentReceipt(ad.getPaymentReceipt())
                        .bank(ad.getBank())
                        .userId(ad.getUser().getId())
                        .userGmail(ad.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    @Operation(summary = "Create a activatePublication")
    @ApiResponse(responseCode = "201", description = "activatePublication created successfully")
    @PostMapping("/activate/{publicationId}")
    public ResponseEntity<String> activatePublication(@PathVariable Long publicationId) {
        managePaymentAdversting.activatePublication(publicationId);
        return ResponseEntity.ok("Публикация активирована успешно.");
    }

    @Operation(summary = "Create a deactivatePublication")
    @ApiResponse(responseCode = "201", description = "deactivatePublication created successfully")
    @PostMapping("/deactivate/{publicationId}")
    public ResponseEntity<String> deactivatePublication(@PathVariable Long publicationId) {
        managePaymentAdversting.deactivatePublication(publicationId);
        return ResponseEntity.ok("Публикация деактивирована успешно.");
    }
}


