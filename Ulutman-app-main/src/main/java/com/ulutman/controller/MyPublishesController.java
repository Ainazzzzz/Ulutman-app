package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.exception.UnauthorizedException;
import com.ulutman.model.dto.AdVersitingResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.User;
import com.ulutman.service.AdVersitingService;
import com.ulutman.service.MyPublishesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/my-publishes")
@Tag(name = "my-publishes")
@SecurityRequirement(name = "Authorization")
public class MyPublishesController {
    private final MyPublishesService publishService;
    private  final AdVersitingService adVersitingService;
    private final MyPublishesService myPublishesService;

    @Operation(summary = "Get all active publications for a user")
    @ApiResponse(responseCode = "201", description = "successfully returned a list of active user publications")
    @GetMapping("/getAllMyPublishes")
    public ResponseEntity<List<PublishResponse>> myPublishes(@AuthenticationPrincipal User user) {
        List<PublishResponse> publishes = publishService.myActivePublishes(user.getId());
        return ResponseEntity.ok(publishes);
    }

    @Operation(summary = "Returns a list of the user's active advertisements")
    @ApiResponse(responseCode = "201", description = "successfully returns a list of the user's active advertisements")
    @GetMapping("/my-ads")
    public List<AdVersitingResponse> getMyAds(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        return publishService.getAllActiveAdsForUser(userId);
    }

    @Operation(summary = "Removes advertisements by user ID")
    @ApiResponse(responseCode = "201", description = "successfully removes advertisements by user ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAd(@PathVariable Long id, @AuthenticationPrincipal User userDetails) {
        Long userId = userDetails.getId();
        boolean deleted = adVersitingService.deleteAd(id, userId);
        if (deleted) {
            return ResponseEntity.ok("Объявление успешно удалено");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Вы не можете удалить это объявление");
        }
    }

    @Operation(summary = "Deletes a user's publishes")
    @ApiResponse(responseCode = "201", description = "Posts successfully deleted")
    @DeleteMapping("/delete-by-user/{userId}")
    public ResponseEntity<String> deletePublishesByUser(
            @PathVariable Long userId,
            @RequestBody Set<Long> publishIds) {
        try {
            publishService.deletePublishesByUser(userId, publishIds);
            return ResponseEntity.ok("Публикации успешно удалены");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Произошла ошибка при удалении публикаций: " + e.getMessage());
        }
    }

    @Operation(summary = "Deletes all user posts")
    @ApiResponse(responseCode = "201", description = "successfully all publications were deleted")
    @DeleteMapping("/delete-all/{userId}")
    public ResponseEntity<String> deleteAllUserPublishes(@PathVariable Long userId) {
        publishService.deleteAllUserPublishes(userId);
        return ResponseEntity.ok("Все публикации успешно удалены");
    }

    @Operation(summary = "Returns a list of inactive user posts")
    @ApiResponse(responseCode = "201", description = "successfully returned a list of inactive user publications")
    @GetMapping("/inactive-publishes/{userId}")
    public ResponseEntity<List<PublishResponse>> getInactivePublishes(@PathVariable Long userId) {
        List<PublishResponse> inactivePublishes = publishService.getInactivePublishes(userId);
        return ResponseEntity.ok(inactivePublishes);
    }

    @Operation(summary = "Edits publication information")
    @ApiResponse(responseCode = "201", description = "successfully edited information about the publication")
    @PutMapping("/update/{userId}/{publishId}")
    public ResponseEntity<PublishResponse> updatePublish(@PathVariable Long userId, @PathVariable Long publishId, @Valid @RequestBody PublishRequest publishRequest) throws UnauthorizedException, NotFoundException {
        PublishResponse updatedPublish = publishService.updatePublish(userId, publishId, publishRequest);
        return ResponseEntity.ok(updatedPublish);
    }

    @Operation(summary = "Returns a list of user's rejected posts")
    @ApiResponse(responseCode = "201", description = "The list of rejected user publications was successfully restored")
    @GetMapping("/rejected-publishes/{userId}")
    public ResponseEntity<List<PublishResponse>> getRejectedPublishes(@PathVariable Long userId) {
        List<PublishResponse> rejectedPublishes = publishService.getRejectedPublishes(userId);
        return ResponseEntity.ok(rejectedPublishes);
    }

    @Operation(summary = "returns to the client the number of favorites for a specific publication")
    @ApiResponse(responseCode = "201", description = "The number of favorites for a specific publication was successfully returned to the client")
    @GetMapping("/count")
    public ResponseEntity<Integer> getFavoritesCount(
            @RequestParam Long userId,
            @RequestParam Long publishId) {
        try {
            Integer count = publishService.getFavoritesCount(userId, publishId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (NotFoundException | UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/boost/{publishId}")
    public ResponseEntity<PublishResponse> boostPublication(@PathVariable Long publishId, @RequestParam Long userId) {
        try {
            PublishResponse response = myPublishesService.boostPublication(userId, publishId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); //или более специфичный ответ об ошибке
        }
    }

    @PutMapping("/boostAdversting/{adId}")
    public ResponseEntity<AdVersitingResponse> boostAdversting(@PathVariable Long adId, @RequestParam Long userId) {
        try {
            AdVersitingResponse response = myPublishesService.boostAdversting(userId, adId);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build(); //или более специфичный ответ об ошибке
        }
    }
}
