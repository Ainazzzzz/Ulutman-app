package com.ulutman.mapper;

import com.ulutman.model.dto.*;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PublishMapper {

    public Publish mapToEntity(PublishRequest publishRequest) {
        Publish publish = new Publish();
        publish.setTitle(publishRequest.getTitle());
        publish.setDescription(publishRequest.getDescription());
        publish.setMetro(publishRequest.getMetro());
        publish.setAddress(publishRequest.getAddress());
        publish.setPhone(publishRequest.getPhoneNumber());
        publish.setImages(publishRequest.getImages());
        publish.setPrice(publishRequest.getPrice());
        publish.setCategory(publishRequest.getCategory());
        publish.setSubCategory(publishRequest.getSubcategory());
        publishRequest.getBank().ifPresent(publish::setBank);
        publishRequest.getPaymentReceiptFile().ifPresent(paymentReceiptUrl -> publish.setPaymentReceiptUrl(String.valueOf(paymentReceiptUrl)));
//        publish.setPublishStatus(publishRequest.getPublishStatus());
        publish.setCategory(publishRequest.getCategory());
        publish.setCreateDate(LocalDate.now());
        return publish;
    }

    private String saveImage(MultipartFile image) {
        return "/////path////to/////saved//////image//////"; // Верните фактический путь к сохраненному изображению
    }

    public PublishResponse mapToResponse(Publish publish) {
        return PublishResponse.builder()
                .id(publish.getId())
                .title(publish.getTitle())
                .description(publish.getDescription())
                .category(publish.getCategory())
                .subcategory(publish.getSubCategory())
                .address(publish.getAddress())
                .phoneNumber(publish.getPhone())
                .images(publish.getImages())
                .price(publish.getPrice())
                .publishStatus(publish.getPublishStatus())
                .createDate(publish.getCreateDate())
                .detailFavorite(publish.isDetailFavorite())
                .categoryStatus(publish.getCategoryStatus())
                .publishStatus(publish.getPublishStatus())
                .user(mapUserToAuthResponse(publish.getUser()))
                .active(publish.isActive())
                .propertyDetails(publish.getPropertyDetails())
                .conditions(publish.getConditions())
                .accountId(publish.getUser().getUserAccount().getId())
                .favoriteCount(publish.getFavoriteCount())
                .metroStation(publish.getMetroStation())
                .build();
    }

    public PublishDetailsResponse mapToDetailsResponse(Publish publish) {
        User user = publish.getUser();
        return PublishDetailsResponse.builder()
                .id(publish.getId())
                .userName(user != null ? user.getName() : "Неизвестно")
                .email(user != null ? user.getEmail() : "Неизвестно")
                .category(publish.getCategory())
                .createDate(publish.getCreateDate())
                .publishStatus(publish.getPublishStatus())
                .build();
    }

    public FilteredPublishResponse mapToFilteredResponse(Publish publish) {
        User user = publish.getUser();
        String userNameResult = user != null ? user.getName() : "Неизвестно";
        return FilteredPublishResponse.builder()
                .id(publish.getId())
                .userName(userNameResult)
                .title(publish.getTitle())
                .description(publish.getDescription())
                .publicationCount(user.getNumberOfPublications())
                .category(publish.getCategory())
                .createDate(publish.getCreateDate())
                .categoryStatus(publish.getCategoryStatus())
                .build();
    }

    private AuthResponse mapUserToAuthResponse(User user) {
        if (user == null) {
            return null;
        }
        return AuthResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
