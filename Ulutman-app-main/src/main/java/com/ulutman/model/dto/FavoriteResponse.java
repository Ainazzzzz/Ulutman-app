package com.ulutman.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class FavoriteResponse {

    Long id;

    PublishResponse publishResponse;
}
