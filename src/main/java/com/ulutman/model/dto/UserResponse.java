package com.ulutman.model.dto;

import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
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
public class UserResponse {

    Long id;

    String userName;

    String email;

    Role role;

    LocalDate createDate;

    Status status;
}
