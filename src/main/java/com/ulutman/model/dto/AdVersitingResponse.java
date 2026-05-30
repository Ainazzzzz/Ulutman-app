package com.ulutman.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AdVersitingResponse {

    Long id;

    String imageFile;

    boolean active;

    String paymentReceipt;

    String bank;

    LocalDateTime nextBoostTime;

    String timeToNextBoost;

    Long userId;

    String userGmail;
}
