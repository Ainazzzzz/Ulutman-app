package com.ulutman.controller;

import com.ulutman.model.dto.MessageRequest;
import com.ulutman.model.dto.MessageResponse;
import com.ulutman.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/messages")
@Tag(name = "Message")
@SecurityRequirement(name = "Authorization")
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "Create  a message")
    @ApiResponse(responseCode = "201", description = "The message created successfully")
    @PostMapping("/create")
    public ResponseEntity<MessageResponse> addMessage(@RequestBody MessageRequest messageRequest) {
        MessageResponse messageResponse = messageService.createMessage(messageRequest);
        return ResponseEntity.ok(messageResponse);
    }

    @Operation(summary = "Update a message status")
    @ApiResponse(responseCode = "201", description = "Updated message status successfully")
    @PutMapping("/{messageId}/status")
    public ResponseEntity<MessageResponse> updateMessageStatus(@PathVariable Long messageId, @RequestBody MessageRequest messageRequest) {
        MessageResponse updatedMessage = messageService.updateMessageStatus(messageId, messageRequest);
        return ResponseEntity.ok(updatedMessage);
    }
}
