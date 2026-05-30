package com.ulutman.mapper;

import com.ulutman.model.dto.AdVersitingResponse;
import com.ulutman.model.entities.AdVersiting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdVersitingMapper {

    public AdVersitingResponse mapToResponse(AdVersiting adVersiting) {
        return AdVersitingResponse.builder()
                .id(adVersiting.getId())
                .imageFile(adVersiting.getImagePath())
                .build();
    }

}
