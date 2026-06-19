package com.ulutman.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Setter
@Getter
public class FavoriteResponseList {

    private Long id;

    private List<PublishResponse> publishResponseList;
}
