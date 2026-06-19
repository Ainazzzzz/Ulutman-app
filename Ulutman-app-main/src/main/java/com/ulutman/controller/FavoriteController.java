package com.ulutman.controller;

import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.dto.FavoriteResponseList;
import com.ulutman.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Favorite")
@SecurityRequirement(name = "Authorization")
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(summary = "Add to Favorites")
    @ApiResponse(responseCode = "201", description = "The publish added  successfully")
    @PostMapping("/addToFavorites/{id}")
    public FavoriteResponse addToFavorites(@PathVariable("id") Long publishId, Principal principal) {
        return favoriteService.addToFavorites(publishId, principal);
    }

    @Operation(summary = "Get all publishes from  Favorites")
    @ApiResponse(responseCode = "201", description = "Return list of publishes")
    @GetMapping("/getAllFavorites")
    public FavoriteResponseList getAllFavorites(Principal principal) {
        return favoriteService.getAllFavorites(principal);
    }

    @Operation(summary = "Delete publish by id  from  Favorites")
    @ApiResponse(responseCode = "201", description = "The publication  successfully deleted from favorites")
    @DeleteMapping("/deleteFromFavorites/{id}")
    public ResponseEntity<String> deleteById(@PathVariable("id") Long publishId, Principal principal) {
        favoriteService.deleteFromFavorites(publishId, principal);
        return ResponseEntity.ok("Публикация, успешно удаленная из избранного");
    }

    @Operation(summary = "Delete all publishes  from  Favorites")
    @ApiResponse(responseCode = "201", description = "All publications  successfully deleted from favorites")
    @PostMapping("/deleteAllFavorites")
    public ResponseEntity<String> deleteAll(Principal principal) {
        favoriteService.deleteAllFavorites(principal);
        return ResponseEntity.ok("Ваш список избранного был успешно очищен");
    }

    @Operation(summary = "Check favorites status")
    @ApiResponse(responseCode = "201", description = "Checked favorites status  successfully")
    @GetMapping("/favorites/check")
    public ResponseEntity<Boolean> isPublishInFavorites(@RequestParam Long productId, Principal principal) {
        boolean isInFavorites = favoriteService.isPublishInFavorites(productId, principal);
        return ResponseEntity.ok(isInFavorites);
    }
}
