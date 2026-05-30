package com.ulutman.mapper;

import com.ulutman.model.dto.BankCardRequest;
import com.ulutman.model.dto.BankCardResponse;

import com.ulutman.model.entities.BankCard;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BankCardMapper {

    public BankCard mapToEntity(BankCardRequest bankCardRequest) {
        BankCard bankCard = new BankCard();
        bankCard.setBankName(bankCardRequest.getBankName());
        bankCard.setCardNumber(bankCardRequest.getCardNumber());
        return bankCard;
    }

    public BankCardResponse mapToResponse(BankCard bankCard) {
        return BankCardResponse.builder()
                .id(bankCard.getId())
                .bankName(bankCard.getBankName())
                .cardNumber(bankCard.getCardNumber())
                .build();
    }
}
