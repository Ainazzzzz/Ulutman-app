package com.ulutman.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankCardDTO {
    private String cardNumber;
    private String cardName;

    public BankCardDTO(String cardNumber, String cardName) {
        this.cardNumber = cardNumber;
        this.cardName = cardName;
    }
}
