package com.ulutman.mapper;

import com.ulutman.model.dto.FavoriteResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Favorite;
import com.ulutman.model.entities.Publish;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FavoriteMapper {

    private final PublishMapper publishMapper;

    public FavoriteResponse mapToResponse(Favorite favorite, Publish publish) {
        return FavoriteResponse.builder()
                .id(favorite.getId())
                .publishResponse(publishMapper.mapToResponse(publish))
                .build();
}
    public List<PublishResponse> mapListToResponseList(List<Publish> publishes) {
        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
