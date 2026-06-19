package com.ulutman.model.dto;

import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailingResponse {

    Long id;

    String title;

    String message;

    MailingType mailingType;

    MailingStatus mailingStatus;

    String image;

    LocalDate promotionStartDate;

    LocalDate promotionEndDate;

    LocalDate createDate;

    List<UserMailingResponse> recipients;
}
