package com.ulutman.controller;

import com.ulutman.model.dto.AuthRequest;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
@Slf4j
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
@CrossOrigin(origins = "https://backend.ulutman.com")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "Create a new Admin")
    @ApiResponse(responseCode = "201", description = "Admin created successfully")
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> saveAdmin(@RequestBody AuthRequest authRequest) {
        AuthResponse response = adminService.saveAdmin(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
