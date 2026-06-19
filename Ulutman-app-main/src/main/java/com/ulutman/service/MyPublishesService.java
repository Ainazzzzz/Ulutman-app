package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.exception.UnauthorizedException;
import com.ulutman.mapper.AdVersitingMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.AdVersitingResponse;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.AdVersiting;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPublishesService {

    private final UserRepository userRepository;
    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final FavoriteRepository favoriteRepository;
    private final AdVersitingRepository adVersitingRepository;
    private final MyPublishRepository myPublishRepository;
    private final AdVersitingMapper adVersitingMapper;

    public List<PublishResponse> myActivePublishes(Long userId) {
        List<Publish> myActivePublish = publishRepository.findAllActivePublishesByUserId(userId);

        return myActivePublish.stream()
                .map(this::mapToResponseWithNextBoost)
                .collect(Collectors.toList());
    }

    public List<AdVersitingResponse> getAllActiveAdsForUser(Long userId) {
        List<AdVersiting> myActivePublish = adVersitingRepository.findAllActiveAdvertingByUserId(userId);


        return myActivePublish.stream()
                .map(this::mapToAdResponseWithNextBoost)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deletePublishesByUser(Long userId, Set<Long> publishIds) {
        List<Publish> userPublishes = userRepository.getAllPublishByUserId(userId);


        userPublishes.removeIf(publish -> !publishIds.contains(publish.getId()));


        List<Long> publishIdsToDelete = userPublishes.stream()
                .map(Publish::getId)
                .collect(Collectors.toList());

        myPublishRepository.deleteAllByPublishIds(publishIdsToDelete);

        publishRepository.deleteAll(userPublishes);
    }

    @Transactional
    public void deleteAllUserPublishes(Long userId) {
        List<Publish> userPublishes = userRepository.getAllPublishByUserId(userId);
        publishRepository.deleteAll(userPublishes);
    }

    public List<PublishResponse> getInactivePublishes(Long userId) {
        List<Publish> inactivePublishes = publishRepository.findByUserIdAndActiveFalse(userId);
        return inactivePublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public PublishResponse updatePublish(Long userId, Long publishId, PublishRequest publishRequest) throws UnauthorizedException, NotFoundException {
        Publish existingPublish = publishRepository.findById(publishId)
                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));


        if (!existingPublish.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Недостаточно прав для редактирования этой публикации");
        }

        existingPublish.setTitle(publishRequest.getTitle());
        existingPublish.setDescription(publishRequest.getDescription());
        existingPublish.setPrice(publishRequest.getPrice());
        existingPublish.setCategory(publishRequest.getCategory());
        existingPublish.setSubCategory(publishRequest.getSubcategory());
        existingPublish.setMetro(publishRequest.getMetro());
        existingPublish.setAddress(publishRequest.getAddress());
        existingPublish.setPublishStatus(publishRequest.getPublishStatus());
        Publish updatedPublish = publishRepository.save(existingPublish);
        return publishMapper.mapToResponse(updatedPublish);
    }

    public List<PublishResponse> getRejectedPublishes(Long userId) {
        List<Publish> rejectedPublishes = publishRepository.findByUserIdAndStatus(userId, PublishStatus.ОТКЛОНЕН);

        return rejectedPublishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public Integer getFavoritesCount(Long userId, Long publishId) {
        Publish publish = publishRepository.findById(publishId)
                .orElseThrow(() -> new NotFoundException("Публикация не найдена"));

        if (!publish.getUser().getId().equals(userId)) {
            throw new UnauthorizedException("Недостаточно прав для просмотра статистики этой публикации");
        }

        Long count = favoriteRepository.countByPublishId(publishId);

        return count.intValue();
    }

    public PublishResponse boostPublication(Long userId, Long publishId) {
        Publish publish = publishRepository.findByIdAndUserId(publishId, userId);

        if (publish == null) {
            throw new EntityNotFoundException("Publication not found");
        }

        boostIfNeeded(publish);
        publishRepository.save(publish);

        return mapToResponseWithNextBoost(publish);
    }

    public AdVersitingResponse boostAdversting(Long userId, Long adId) {
        AdVersiting ad = adVersitingRepository.findByIdAndUserId(adId, userId);

        if (ad == null) {
            throw new EntityNotFoundException("Advertisement not found");
        }

        boostIfNeeded(ad);

        return mapToAdResponseWithNextBoost(ad); // Новый метод для маппинга
    }


    private void boostIfNeeded(Publish publish) {
        LocalDateTime now = LocalDateTime.now();
        if (publish.getLastBoostedAt() == null || ChronoUnit.HOURS.between(publish.getLastBoostedAt(), now) >= 24) {
            publish.setLastBoostedAt(now);
            publishRepository.save(publish);
        }
    }

    private void boostIfNeeded(AdVersiting ad) {
        LocalDateTime now = LocalDateTime.now();
        if (ad.getLastBoostedTime() == null || ChronoUnit.HOURS.between(ad.getLastBoostedTime(), now) >= 24) {
            ad.setLastBoostedTime(now);
            adVersitingRepository.save(ad);
        }
    }

    private PublishResponse mapToResponseWithNextBoost(Publish publish) {
        PublishResponse response = publishMapper.mapToResponse(publish);
        response.setNextBoostTime(calculateNextBoostTime(publish));
        response.setTimeToNextBoost(formatTimeToNextBoost(response.getNextBoostTime()));
        return response;
    }

    private AdVersitingResponse mapToAdResponseWithNextBoost(AdVersiting ad) {
        AdVersitingResponse adVersitingResponse = adVersitingMapper.mapToResponse(ad);
        adVersitingResponse.setNextBoostTime(calculateNextBoostTimeAdcersting(ad));
        adVersitingResponse.setTimeToNextBoost(formatTimeToNextBoost(adVersitingResponse.getNextBoostTime()));
        return adVersitingResponse;
    }

    private String formatTimeToNextBoost(LocalDateTime nextBoostTime) {
        if (nextBoostTime == null) return "";

        Duration duration = Duration.between(LocalDateTime.now(), nextBoostTime);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();

        StringBuilder timeString = new StringBuilder();

        if (hours > 0) {
            timeString.append(hours).append("ч ").append(minutes).append("мин");
        } else if (minutes > 0) {
            timeString.append(minutes).append("мин");
        } else {
            return "Сейчас";
        }

        return   "можно поднимать через: " + timeString;
    }

    private LocalDateTime calculateNextBoostTime(Publish publish) {
        LocalDateTime lastBoostedAt = publish.getLastBoostedAt();
        if (lastBoostedAt == null) {
            return LocalDateTime.now().plusHours(24);
        } else {
            LocalDateTime nextBoost = lastBoostedAt.plusHours(24);
            return nextBoost.isBefore(LocalDateTime.now()) ? LocalDateTime.now().plusHours(24) : nextBoost; //Если уже пора бустить - через 24 часа от СЕЙЧАС
        }
    }

    private LocalDateTime calculateNextBoostTimeAdcersting(AdVersiting publish) {
        LocalDateTime lastBoostedAt = publish.getLastBoostedAt();
        if (lastBoostedAt == null) {
            return LocalDateTime.now().plusHours(24);
        } else {
            LocalDateTime nextBoost = lastBoostedAt.plusHours(24);
            return nextBoost.isBefore(LocalDateTime.now()) ? LocalDateTime.now().plusHours(24) : nextBoost; //Если уже пора бустить - через 24 часа от СЕЙЧАС
        }
    }
}
