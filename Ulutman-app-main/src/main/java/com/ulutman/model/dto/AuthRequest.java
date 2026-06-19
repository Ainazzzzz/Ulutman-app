package com.ulutman.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.ulutman.model.enums.Role;
import com.ulutman.model.enums.Status;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(min = 3, max = 20, message = "Name should be between 6 and 20 characters!")
    String name;

    @NotBlank(message = "Email не должен быть пустым")
    @Email(message = "Некорректный формат email")
    @Size(min = 5, max = 30, message = "email must be between 5 and 30 characters!")
    String email;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).{6,}$",
            message = "Пароль должен содержать минимум 6 символов, хотя бы одну букву и одну цифру"
    )
    @Size(min = 6, max = 20, message = "password must be between 6 and 20 characters!")
    String password;

    @NotBlank(message = "Подтверждение пароля не должно быть пустым")
    String confirmPassword;

    @JsonDeserialize(using = RoleDeserializer.class)
    Role role;

    Status status;

    @CreatedDate
    LocalDate createDate;
}
