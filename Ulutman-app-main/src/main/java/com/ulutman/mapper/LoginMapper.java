package com.ulutman.mapper;

import com.ulutman.model.dto.LoginResponse;
import com.ulutman.model.entities.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {

    public LoginResponse mapToResponse(String token, User user) {
        return LoginResponse.builder()
                .token(token)
                .roleName(user.getRole())
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .createDate(user.getCreateDate())
                .build();
    }
}
