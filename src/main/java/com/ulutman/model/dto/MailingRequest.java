package com.ulutman.model.dto;

import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MailingRequest {

    String title;

    String message;

    MailingType mailingType;

    MailingStatus mailingStatus;

    LocalDate promotionStartDate;

    LocalDate promotionEndDate;

    LocalDate createDate;

    List<Long> recipientIds;
}
