package com.ulutman.controller;

import com.ulutman.model.dto.AdVersitingResponse;
import com.ulutman.model.entities.AdVersiting;
import com.ulutman.service.AdVersitingService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/advertising")
@Tag(name = "AdVersting")
@SecurityRequirement(name = "Authorization")
@CrossOrigin(origins = "*",maxAge = 3600)
public class AdvertisingController {


    private final AdVersitingService adVersitingService;


    public AdvertisingController(AdVersitingService adVersitingService) {
        this.adVersitingService = adVersitingService;
    }


    @Operation(summary = "Create a Adversting")
    @ApiResponse(responseCode = "201", description = "successfully create a Adversting")
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<String> createAdvertising(@RequestParam("imageFile") MultipartFile imageFile,
                                                    @RequestParam("bank") String bank,
                                                    @RequestParam("paymentReceiptFile") MultipartFile paymentReceiptFile,
                                                    Principal principal) {
        System.out.println("Received bank: " + bank);
        System.out.println("Received imageFile: " + (imageFile != null ? imageFile.getOriginalFilename() : "null"));
        System.out.println("Received paymentReceiptFile: " + (paymentReceiptFile != null ? paymentReceiptFile.getOriginalFilename() : "null"));
        try {
            adVersitingService.createAdvertising(imageFile, bank, paymentReceiptFile, principal);
            return ResponseEntity.status(HttpStatus.CREATED).body("Реклама успешно создана");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при обработке файлов: " + e.getMessage());
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ошибка при отправке уведомления: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Неизвестная ошибка: " + e.getMessage());
        }
    }

    @Operation(summary = "returns all active posts")
    @ApiResponse(responseCode = "201", description = "successfully returns all active posts")
    @GetMapping
    public ResponseEntity<List<AdVersitingResponse>> getAllAds() {
        List<AdVersiting> ads = adVersitingService.getAllActiveAds();
        List<AdVersitingResponse> responseList = ads.stream()
                .map(ad -> AdVersitingResponse.builder()
                        .id(ad.getId())
                        .imageFile(ad.getImagePath())
                        .active(ad.isActive())
                        .paymentReceipt(ad.getPaymentReceipt())
                        .bank(ad.getBank())
                        .userId(ad.getUser().getId())
                        .userGmail(ad.getUser().getEmail())
                        .build())
                .collect(Collectors.toList());


        return ResponseEntity.ok(responseList);
    }
}
