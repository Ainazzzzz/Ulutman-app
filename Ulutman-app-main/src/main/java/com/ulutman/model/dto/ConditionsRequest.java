package com.ulutman.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConditionsRequest {

    Double pricePerMonth;

    String utilitiesIncluded;

    Double deposit;

    String commission;

    String prepayment;

    String leaseTerm;

    boolean showPhoneNumber;

    String realtor;

    Double realtorRating;

    ConditionsRequest conditions;
}
