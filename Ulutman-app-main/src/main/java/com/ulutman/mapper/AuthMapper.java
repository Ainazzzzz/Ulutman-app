package com.ulutman.mapper;

import com.ulutman.model.dto.*;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthMapper {

    private final PublishMapper publishMapper;

    public User mapToEntity(AuthRequest authRequest) {
        User user = new User();
        user.setName(authRequest.getName());
        user.setEmail(authRequest.getEmail());
        if (!authRequest.getPassword().equals(authRequest.getConfirmPassword())) {
            throw new RuntimeException("Пароли не совпадают");
        }
        user.setRole(Role.USER);
        return user;
    }

    public AuthResponse mapToResponse(User user) {

        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<PublishResponse> publishResponses = user.getPublishes() != null ?
                user.getPublishes().stream()
                        .map(publishMapper::mapToResponse)
                        .collect(Collectors.toList())
                : Collections.emptyList();

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .createDate(user.getCreateDate())
                .publishes(publishResponses)
                .build();
    }

    public AuthResponse mapToResponseWithToken(String jwt, User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }

        List<PublishResponse> publishResponses = user.getPublishes() != null
                ? user.getPublishes().stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .status(user.getStatus())
                .role(user.getRole())
                .createDate(user.getCreateDate())
                .publishes(publishResponses)
                .numberOfPublications(publishResponses.size())
                .token(jwt)
                .build();
    }


    public UserResponse mapToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .createDate(user.getCreateDate())
                .status(user.getStatus())
                .build();
    }


    public AuthResponse mapToComplaintResponse(User user) {
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
