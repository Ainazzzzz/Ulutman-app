package com.ulutman.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ulutman.model.entities.Conditions;
import com.ulutman.model.entities.PropertyDetails;
import com.ulutman.model.enums.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishResponse {

    Long id;

    String title;

    String description;

    String metroStation;

    String address;

    String phoneNumber;

    List<String> images;

    double price;

    Category category;

    Integer numberOfPublications;

    Subcategory subcategory;

    PublishStatus publishStatus;

    LocalDate createDate;

    boolean detailFavorite;

    @JsonBackReference
    AuthResponse user;

    CategoryStatus categoryStatus;

    PropertyDetails propertyDetails;

    boolean active;

    Conditions conditions;

    LocalDateTime nextBoostTime;

    String timeToNextBoost;

    Long accountId;

    Long favoriteCount;

}
