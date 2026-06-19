package com.ulutman.controller;

import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.Metro;
import com.ulutman.model.enums.Subcategory;
import com.ulutman.service.MainPageService;
import com.ulutman.service.PublishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main-page")
@Tag(name = "Main - Page")
@SecurityRequirement(name = "Authorization")
public class MainPageController {

    private final MainPageService mainPageService;

    private final PublishService publishService;

    @Operation(summary = "Get publishes by category WORK")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category WORK")
    @GetMapping("/work")
    List<PublishResponse> getPublishByCategoryWork() {
        return mainPageService.findPublishByCategoryWork();
    }

    @Operation(summary = "Get publishes by subCategory WORK")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory WORK")
    @GetMapping("/work/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategoryWORK(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishByCategoryWORK(subCategory);
    }

    @Operation(summary = "Get publishes by category RENT")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category RENT")
    @GetMapping("/rent")
    List<PublishResponse> getPublishByCategoryRent() {
        return mainPageService.findPublishByCategoryRent();
    }

    @Operation(summary = "Get publishes by subCategory RENT")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory RENT")
    @GetMapping("/rent/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategoryRENT(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishByCategoryRent(subCategory);
    }

    @Operation(summary = "Get publishes by category SELL")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category SELL")
    @GetMapping("/sell")
    List<PublishResponse> getPublishByCategorySell() {
        return mainPageService.findPublishByCategorySell();
    }

    @Operation(summary = "Get publishes by subCategory HOTEL")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory HOTEL")
    @GetMapping("/sell/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategorySELL(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishByCategorySell(subCategory);
    }

    @Operation(summary = "Get publishes by category HOTEL")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category HOTEL")
    @GetMapping("/hotel")
    List<PublishResponse> getPublishByCategoryHotel() {
        return mainPageService.findPublishByCategoryHotel();
    }

    @Operation(summary = "Get publishes by subCategory SELL")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory SELL")
    @GetMapping("/hotel/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategoryHOTEL(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishByCategoryHotel(subCategory);
    }

    @Operation(summary = "Get publishes by category SERVICES")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category SERVICES")
    @GetMapping("/services")
    List<PublishResponse> getPublishByCategoryServices() {
        return mainPageService.findPublishByCategoryServices();
    }

    @Operation(summary = "Get publishes by subCategory SERVICES")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory SERVICES")
    @GetMapping("/services/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategoryServices(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishByCategoryServices(subCategory);
    }

    @Operation(summary = "Get publishes by subCategory AUTO")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory AUTO")
    @GetMapping("/auto/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategoryAuto(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishByCategoryServices(subCategory);
    }

    @Operation(summary = "Get publishes by category REAL_ESTATE")
    @ApiResponse(responseCode = "201", description = "return list of publishes by category REAL_ESTATE")
    @GetMapping("/realEstate")
    List<PublishResponse> getPublishByCategoryRealEstate() {
        return mainPageService.findPublishByCategoryRealEstate();
    }

    @Operation(summary = "Get publishes by subCategory REAL_ESTATE")
    @ApiResponse(responseCode = "201", description = "return list of publishes by subCategory REAL_ESTATE")
    @GetMapping("/real-estate/subcategory/{subCategory}")
    public List<PublishResponse> getPublishesBySubCategoryREAL_ESTATE(@PathVariable Subcategory subCategory) {
        return mainPageService.findPublishBySubCategoryREAL_ESTATE(subCategory);
    }

    @Operation(summary = "Filter categories by criteria ")
    @ApiResponse(responseCode = "201", description = "Categories successfully filtered")
    @GetMapping("/filter")
    public List<PublishResponse> filterPublishes(
            @RequestParam(required = false) List<Category> categories,
            @RequestParam String sortBy) {
        return mainPageService.filterPublishesByCategory(categories, sortBy);
    }

    @Operation(summary = "Search  publications by criteria")
    @ApiResponse(responseCode = "201", description = "Search publications  successfully")
    @GetMapping("/search")
    public List<PublishResponse> filterPublishes(
            @RequestParam(required = false) List<Category> categories,
            @RequestParam(required = false) List<String> titles,
            @RequestParam(required = false) List<Metro> metros) {

        return mainPageService.searchPublishes(categories, titles, metros);
    }

    @Operation(summary = "Reset filters publications")
    @ApiResponse(responseCode = "200", description = "Publishes filters successfully reset")
    @GetMapping("/resetFilter")
    public List<PublishResponse> resetFilter(Principal principal) {
        return publishService.getAll(principal);
    }


    @Operation(summary = "Get all metro ")
    @ApiResponse(responseCode = "201", description = "Returned all metro by id successfully")
    @GetMapping("/all/metro")
    public List<Map<String, String>> getAllMetroStations() {
        List<Map<String, String>> metroStations = new ArrayList<>();

        for (Metro metro : Metro.values()) {
            Map<String, String> station = new HashMap<>();
            station.put("id", String.valueOf(metro.ordinal() + 1));
            station.put("value", metro.name());
            station.put("label", metro.getLabel());
            metroStations.add(station);
        }

        return metroStations;
    }
}
