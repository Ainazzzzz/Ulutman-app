package com.ulutman.model.dto;

import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class LoginResponse {

    private String token;

    private Role roleName;

    private Long userId;

    private String name;

    private String email;

    private Status status;

    private LocalDate createDate;
}
