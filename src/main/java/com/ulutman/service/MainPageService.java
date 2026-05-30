package com.ulutman.service;

import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.PublishSpecification;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.Metro;
import com.ulutman.model.enums.Subcategory;
import com.ulutman.repository.PublishRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MainPageService {

    private final PublishRepository publishRepository;
    private final PublishMapper publishMapper;

    public List<PublishResponse> findPublishByCategoryWork() {
        List<Publish> work = publishRepository.findByCategoryWork();
        return work.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryWORK(Subcategory subCategory) {
        List<Publish> work = publishRepository.findBySubCategoryWORK(subCategory);
        return work.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryRent() {
        List<Publish> rent = publishRepository.findByCategoryRent();
        return rent.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryRent(Subcategory subCategory) {
        List<Publish> rent = publishRepository.findBySubCategoryRent(subCategory);
        return rent.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategorySell() {
        List<Publish> sell = publishRepository.findByCategorySell();
        return sell.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategorySell(Subcategory subCategory) {
        List<Publish> sell = publishRepository.findBySubCategorySell(subCategory);
        return sell.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryHotel() {
        List<Publish> hotel = publishRepository.findByCategoryHotel();
        return hotel.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryHotel(Subcategory subCategory) {
        List<Publish> hotel = publishRepository.findBySubCategoryHotel(subCategory);
        return hotel.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryServices() {
        List<Publish> services = publishRepository.findByCategoryServices();
        return services.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryServices(Subcategory subCategory) {
        List<Publish> services = publishRepository.findBySubCategoryServices(subCategory);
        return services.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryAUTO(Subcategory subCategory) {
        List<Publish> auto = publishRepository.findBySubCategoryAUTO(subCategory);
        return auto.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishByCategoryRealEstate() {
        List<Publish> realEstate = publishRepository.findByCategoryRealEstate();
        return realEstate.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> findPublishBySubCategoryREAL_ESTATE(Subcategory subCategory) {
        List<Publish> publishes = publishRepository.findBySubCategoryREAL_ESTATE(subCategory);
        return publishes.stream().map(publishMapper::mapToResponse).toList();
    }

    public List<PublishResponse> filterPublishesByCategory(List<Category> categories, String sortBy) {
        List<Publish> publishes = publishRepository.filterPublishesByCategory(categories, sortBy);
        return publishes.stream()
                .map(publishMapper::mapToResponse) // Преобразование Publish в PublishResponse
                .collect(Collectors.toList());
    }

    public List<PublishResponse> searchPublishes(List<Category> categories,
                                                 List<String> titles,
                                                 List<Metro> metros) {
        Specification<Publish> specification = Specification
                .where(PublishSpecification.hasCategoryIn(categories))
                .and(PublishSpecification.hasTitleContaining(titles))
                .and(PublishSpecification.hasMetroIn(metros));

        List<Publish> filteredPublishes = publishRepository.findAll(specification);

        return filteredPublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
