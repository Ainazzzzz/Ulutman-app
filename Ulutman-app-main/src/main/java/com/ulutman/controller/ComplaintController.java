package com.ulutman.controller;

import com.ulutman.model.dto.ComplaintRequest;
import com.ulutman.model.dto.ComplaintResponse;
import com.ulutman.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/complaints")
@Tag(name = "Complaint")
@SecurityRequirement(name = "Authorization")
public class ComplaintController {

    private final ComplaintService complaintService;

    @Operation(summary = "Create a complaint")
    @ApiResponse(responseCode = "201", description = "The complaint created successfully")
    @PostMapping("/create")
    public ResponseEntity<ComplaintResponse> createComplaint(@RequestBody ComplaintRequest complaintRequest) {
        ComplaintResponse response = complaintService.createComplaint(complaintRequest);
        return ResponseEntity.ok(response);
    }
}
