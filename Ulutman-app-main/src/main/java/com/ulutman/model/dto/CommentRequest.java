package com.ulutman.model.dto;

import com.ulutman.model.enums.ModeratorStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {

    Long userId;

    String content;

    ModeratorStatus moderatorStatus;
}
