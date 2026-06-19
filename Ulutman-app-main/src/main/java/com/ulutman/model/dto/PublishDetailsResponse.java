package com.ulutman.model.dto;

import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
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
public class PublishDetailsResponse {

    Long id;

    String userName;

    String email;

    Category category;

    LocalDate createDate;

    PublishStatus publishStatus;
}
