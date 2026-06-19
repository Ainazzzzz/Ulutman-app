package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.AuthMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.AuthResponse;
import com.ulutman.model.dto.FilteredPublishResponse;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.CategoryStatus;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageCategoryService {

    private final PublishRepository publishRepository;
    private final PublishMapper publishMapper;
    private final UserRepository userRepository;
    private final AuthMapper authMapper;

    public List<FilteredPublishResponse> getAllCategory() {
        return publishRepository.findAll().stream().map(publishMapper::mapToFilteredResponse).collect(Collectors.toList());
    }

    public List<AuthResponse> getAllUsersWithPublishes() {
        List<User> users = userRepository.findAll();

        return users.stream()
                .filter(user -> !publishRepository.findAllByUserId(user.getId()).isEmpty())
                .map(user -> {
                    List<PublishResponse> publishes = publishRepository.findAllByUserId(user.getId())
                            .stream()
                            .map(publishMapper::mapToResponse)
                            .collect(Collectors.toList());

                    AuthResponse authResponse = authMapper.mapToResponse(user);
                    authResponse.setPublishes(publishes);
                    authResponse.setNumberOfPublications(publishes.size()); // Устанавливаем количество публикаций

                    return authResponse;
                })
                .collect(Collectors.toList());
    }

    public FilteredPublishResponse updateCategoryStatus(Long id, CategoryStatus newStatus) {
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Публикация по идентификатору " + id + " не найдена"));

        if (newStatus != null && !newStatus.equals(publish.getCategoryStatus())) {
            publish.setCategoryStatus(newStatus);
            publishRepository.save(publish);
        }
        return publishMapper.mapToFilteredResponse(publish);
    }

    public List<FilteredPublishResponse> filterPublishes(
            List<Category> categories,
            List<CategoryStatus> categoryStatuses,
            List<LocalDate> createDates,
            String title,
            String names) {
        List<Publish> filteredPublishes = new ArrayList<>();

        if (categories != null && categories.stream().anyMatch(category -> category == null)) {
            throw new IllegalArgumentException("Категории не могут содержать нулевых значений.");
        } else if (categories != null && !categories.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCategory(categories));
        }

        if (categoryStatuses != null && categoryStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        } else if (categoryStatuses != null && !categoryStatuses.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCategoryStatus(categoryStatuses));
        }

        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        } else if (createDates != null && !createDates.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCreateDate(createDates));
        }

        if (title != null && !title.trim().isEmpty()) {
            List<Publish> publishesByTitle = publishRepository.filterPublishesByTitle(title.trim());
            if (!publishesByTitle.isEmpty()) {
                filteredPublishes.addAll(publishesByTitle);
            }
        }

        if (names != null && !names.trim().isEmpty()) {
            List<User> filteredUsers = userRepository.findByUserName(names);
            if (!filteredUsers.isEmpty()) {
                for (User user : filteredUsers) {
                    filteredPublishes.addAll(publishRepository.filterPublishesByUser(user.getId()));
                }
            }

            for (User user : filteredUsers) {
                List<Publish> userPublications = publishRepository.filterPublishesByUser(user.getId());
                if (!userPublications.isEmpty()) {
                    filteredPublishes.addAll(userPublications);
                }
            }

            if (filteredPublishes.isEmpty()) {
                return Collections.emptyList();
            }
        }

        filteredPublishes = filteredPublishes.stream().distinct().collect(Collectors.toList());

        if (filteredPublishes.isEmpty()) {
            return Collections.emptyList();
        }

        return filteredPublishes.stream()
                .map(publish -> {
                    User user = publish.getUser();
                    String userName = user != null ? user.getName() : "Неизвестно";
                    long publicationCount = publishRepository.countByUser(user.getId());

                    return FilteredPublishResponse.builder()
                            .userName(userName)
                            .title(publish.getTitle())
                            .description(publish.getDescription())
                            .publicationCount((int) publicationCount)
                            .category(publish.getCategory())
                            .categoryStatus(publish.getCategoryStatus())
                            .build();
                })
                .collect(Collectors.toList());
    }
}
