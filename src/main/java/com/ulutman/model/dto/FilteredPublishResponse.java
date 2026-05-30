package com.ulutman.model.dto;

import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilteredPublishResponse {

    Long id;

    String userName;

    String title;

    String description;

    Integer publicationCount;

    Category category;

    LocalDate createDate;

    CategoryStatus categoryStatus;
}
