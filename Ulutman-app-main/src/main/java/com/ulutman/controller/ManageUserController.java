package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.model.dto.UserResponse;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import com.ulutman.service.ManageUserService;
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
@RequestMapping("api/manage/users")
@Slf4j
@Tag(name = "Manage User")
@SecurityRequirement(name = "Authorization")
public class ManageUserController {

    private final ManageUserService manageUserService;

    @Operation(summary = "Get all users")
    @ApiResponse(responseCode = "201", description = "Return list if users")
    @GetMapping("/getAll")
    public List<UserResponse> getAll() {
        return manageUserService.getAllUsers();
    }

    @Operation(summary = "Get user by id")
    @ApiResponse(responseCode = "201", description = "User found")
    @GetMapping("/getById/{id}")
    public UserResponse getById(@PathVariable Long id) {
        return manageUserService.getUserById(id);
    }

    @Operation(summary = "Update user status")
    @ApiResponse(responseCode = "201", description = "Updated user status successfully")
    @PutMapping("/{id}/status")
    public ResponseEntity<UserResponse> updateUserStatus(@PathVariable Long id,
                                                         @RequestParam Status newStatus) {
        UserResponse userResponse = manageUserService.updateUserStatus(id, newStatus);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Filter users by criteria")
    @ApiResponse(responseCode = "201", description = "Users successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<UserResponse>> filterUsers(
            @RequestParam(required = false) List<Role> roles,
            @RequestParam(required = false) List<Status> statuses,
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) String names) {

        List<UserResponse> filteredUsers = manageUserService.filterUsers(roles, createDates, statuses, names);

        return ResponseEntity.ok(filteredUsers);
    }

    @Operation(summary = "Reset filters users")
    @ApiResponse(responseCode = "201", description = "Users filters successfully reset")
    @GetMapping("/resetFilter")
    public List<UserResponse> resetFilter() {
        return manageUserService.getAllUsers();
    }

    @Operation(summary = "Delete users by array by id")
    @ApiResponse(responseCode = "201", description = "Deleted users by  array by id successfully")
    @DeleteMapping("/delete/batch")
    public ResponseEntity<String> deleteUsersByIds(@RequestBody List<Long> ids) {
        try {
            manageUserService.deleteUsersByIds(ids);
            return ResponseEntity.ok("Пользователи успешно удалены");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Некоторые пользователи не найдены");
        }
    }
}
