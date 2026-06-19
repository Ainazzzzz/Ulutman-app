package com.ulutman.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
public class AdVersitingRequest {

    private Long id;

    private MultipartFile imageFile;

    private MultipartFile paymentReceipt;

    private String bank;
}
