package com.ulutman.controller;

import com.ulutman.exception.NotFoundException;
import com.ulutman.model.dto.BankCardDTO;
import com.ulutman.model.dto.BankCardRequest;
import com.ulutman.model.entities.BankCard;
import com.ulutman.service.BankCardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank-cards")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankCardController {

    private final BankCardService bankCardService;

    @Operation(summary = "Create a Bank")
    @ApiResponse(responseCode = "201", description = "Bank created successfully")
    @PostMapping
    public ResponseEntity<BankCard> createBankCard(@RequestBody BankCardRequest bankCardRequest) {
        try {
            BankCard createdBankCard = bankCardService.createBankCard(bankCardRequest);
            return new ResponseEntity<>(createdBankCard, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error creating bank card: {}", e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    @Operation(summary = "Create a getAllBanks")
    @ApiResponse(responseCode = "201", description = "GetAllBank created successfully")
    @GetMapping("")
    public ResponseEntity<List<BankCardDTO>> getAllBankCards() {
        List<BankCardDTO> bankCards = bankCardService.getAllBankCards();
        return ResponseEntity.ok(bankCards);
    }

    @Operation(summary = "Create a getBankCardById")
    @ApiResponse(responseCode = "201", description = "getBankCardById created successfully")
    @GetMapping("/{id}")
    public ResponseEntity<BankCard> getBankCardById(@PathVariable Long id) {
        try {
            BankCard bankCard = bankCardService.getBankCardById(id);
            return ResponseEntity.ok(bankCard);
        } catch (NotFoundException e) {
            log.warn("Bank card not found: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a deleteBankCardById")
    @ApiResponse(responseCode = "201", description = "deleteBankCardById created successfully")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankCardById(@PathVariable Long id) {
        try {
            bankCardService.deleteBankCardById(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            log.warn("Attempted to delete non-existing bank card: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
