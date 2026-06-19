package com.ulutman.model.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {

    Long id;

    String name;

    String email;

    Role role;

    Status status;

    LocalDate createDate;

    @JsonManagedReference
    List<PublishResponse> publishes;

    int numberOfPublications;

    String token;
}
