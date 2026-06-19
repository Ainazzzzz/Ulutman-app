package com.ulutman.controller;

import com.ulutman.model.dto.UserAccountUpdateRequest;
import com.ulutman.model.entities.UserAccount;
import com.ulutman.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user-accounts")
@Tag(name = "Profile")
@SecurityRequirement(name = "Authorization")
public class UserAccountController {

    private final ProfileService userAccountService;

    @Operation(summary = "Update a profile")
    @ApiResponse(responseCode = "201", description = "The profile updated successfully")
    @PutMapping("/{userId}")
    public ResponseEntity<UserAccount> updateUserAccount(@PathVariable Long userId,
                                                         @RequestBody UserAccountUpdateRequest request) {
        UserAccount userAccount = userAccountService.updateUserAccount(
                userId,
                request.getEmail(),
                request.getName()
        );
        return new ResponseEntity<>(userAccount, HttpStatus.OK);
    }
}

