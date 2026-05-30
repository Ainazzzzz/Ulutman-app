package com.ulutman.model.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConditionsResponse {

    Long id;

    Double pricePerMonth;

    String utilitiesIncluded;

    Double deposit;

    String commission;

    String prepayment;

    String leaseTerm;

    boolean showPhoneNumber;

    String realtor;

    Double realtorRating;
}
