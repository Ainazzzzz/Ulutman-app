package com.ulutman.model.dto;

import com.ulutman.model.enums.ComplaintStatus;
import com.ulutman.model.enums.ComplaintType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplaintResponse {

    Long id;

    String userName;

    ComplaintType complaintType;

    String complaintContent;

    LocalDate createDate;

    ComplaintStatus complaintStatus;
}
