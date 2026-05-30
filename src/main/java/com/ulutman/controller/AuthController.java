package com.ulutman.controller;

import com.ulutman.exception.PasswordsDoNotMatchException;
import com.ulutman.model.dto.*;
import com.ulutman.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Auth")
@SecurityRequirement(name = "Authorization")
@CrossOrigin(origins = "*",maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Create a new User and return token")
    @ApiResponse(responseCode = "201", description = "User created and return successfully")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody AuthRequest request) {
        AuthResponse response = authService.saveUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Create a new User")
    @ApiResponse(responseCode = "201", description = "User created successfully")
    @PostMapping("/sign-up")
    public ResponseEntity<AuthResponse> save(@RequestBody @Valid AuthRequest authRequest) {
        AuthResponse response = authService.save(authRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login a new User")
    @ApiResponse(responseCode = "201", description = "User login successfully")
    @PostMapping("/sign-in")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/google-login")
    public ResponseEntity<AuthWithGoogleResponse> googleLogin(@RequestParam String token) {
        AuthWithGoogleResponse authResponse = authService.registerUserWithGoogle(token);
        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Send pinCode")
    @ApiResponse(responseCode = "201", description = "Mailing sent pinCode  successfully")
    @GetMapping("/sendPasswordResetCode")
    public ResponseEntity<String>sendPasswordResetCode(@RequestParam String email) throws EntityNotFoundException {
        authService.sendPasswordResetCode(email);
        return ResponseEntity.ok("Пин-код отправлен на вашу почту");
    }

    @Operation(summary = "Reset password")
    @ApiResponse(responseCode = "201", description = "Reset password  successfully")
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam int pinCode,
                                                @RequestParam String newPassword, @RequestParam String confirmPassword)
            throws EntityNotFoundException, PasswordsDoNotMatchException {
        return authService.resetPassword(email, pinCode, newPassword, confirmPassword);
    }
}
