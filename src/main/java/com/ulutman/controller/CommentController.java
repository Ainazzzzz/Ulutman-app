package com.ulutman.controller;

import com.ulutman.model.dto.CommentRequest;
import com.ulutman.model.dto.CommentResponse;
import com.ulutman.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/comments")
@Tag(name = "Comment")
@SecurityRequirement(name = "Authorization")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create a comment")
    @ApiResponse(responseCode = "201", description = "The comment created successfully")
    @PostMapping("/create")
    public ResponseEntity<CommentResponse> addComment(@RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Update comment status")
    @ApiResponse(responseCode = "201", description = "Updated comment status successfully")
    @PutMapping("/{commentId}/status")
    public ResponseEntity<CommentResponse> updateCommentStatus(@PathVariable Long commentId, @RequestBody CommentRequest commentRequest) {
        CommentResponse updatedComment = commentService.updateCommentStatus(commentId, commentRequest);
        return ResponseEntity.ok(updatedComment);
    }
}
