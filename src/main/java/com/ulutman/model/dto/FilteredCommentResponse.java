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
public class FilteredCommentResponse {

    Long id;

    String userName;

    String content;

    LocalDate createDate;

    ModeratorStatus moderatorStatus;
}
