package com.ulutman.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountCreateRequest {
    private Long userId;

    private String username;

    private String emailAddress;
}
