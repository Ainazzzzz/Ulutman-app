package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.model.dto.*;
import com.ulutman.model.enums.ModeratorStatus;
import com.ulutman.service.ManageModeratorService;
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
@RequestMapping("/api/manage/moderator")
@Tag(name = "Manage Moderator")
@SecurityRequirement(name = "Authorization")
public class ManageModeratorController {

    private final ManageModeratorService manageModeratorService;

    @Operation(summary = "Get all comments")
    @ApiResponse(responseCode = "201", description = "Return list of comments")
    @GetMapping("/getAll")
    public List<FilteredCommentResponse> getAll() {
        return manageModeratorService.getAllComments();
    }

    @Operation(summary = "Get comments of user")
    @ApiResponse(responseCode = "201", description = "Return list comments of user")
    @GetMapping("/comments/{userId}")
    public ResponseEntity<UserCommentsResponse> getUserWithComments(@PathVariable Long userId) {
        UserCommentsResponse userWithComments = manageModeratorService.getUserWithComments(userId);
        return ResponseEntity.ok(userWithComments);
    }

    @Operation(summary = "Update comment status")
    @ApiResponse(responseCode = "201", description = "Updated comment status successfully")
    @PutMapping("/update/status/{id}")
    public ResponseEntity<FilteredCommentResponse> updateCommentStatus(
            @PathVariable Long id,
            @RequestParam ModeratorStatus newStatus) {

        FilteredCommentResponse response = manageModeratorService.updateCommentStatus(id, newStatus);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Filter comments by criteria")
    @ApiResponse(responseCode = "201", description = "Comments successfully filtered")
    @GetMapping("/filter")
    public ResponseEntity<List<FilteredCommentResponse>> filterComments(
            @RequestParam(required = false) List<LocalDate> createDates,
            @RequestParam(required = false) List<ModeratorStatus> moderatorStatuses,
            @RequestParam(required = false) String content,
            @RequestParam(required = false) String names) {

        List<FilteredCommentResponse> comments = manageModeratorService.filterComments(createDates, moderatorStatuses, content, names);

        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "Delete comment by Id")
    @ApiResponse(responseCode = "201", description = "Comments successfully deleted")
    @DeleteMapping("/delete/batch")
    public ResponseEntity<String> deleteCommentsByIds(@RequestBody List<Long> ids) {
        try {
            manageModeratorService.deleteCommentsByIds(ids);
            return ResponseEntity.ok("Комментарии успешно удалены");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Некоторые комментарии  не найдены");

        }
    }
}
