package com.ulutman.model.dto;

import com.ulutman.model.enums.ModeratorStatus;
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
public class ModeratorMessageResponse {

    Long messageId;

    String username;

    AuthResponse authResponse;

    String content;

    LocalDate createDate;

    ModeratorStatus moderatorStatus;

    public ModeratorMessageResponse(Long messageId, String username, AuthResponse authResponse, String content, LocalDate createDate, ModeratorStatus moderatorStatus) {
        this.messageId = messageId;
        this.username = username;
        this.authResponse = authResponse;
        this.content = content;
        this.createDate = createDate;
        this.moderatorStatus = moderatorStatus;
    }
}
