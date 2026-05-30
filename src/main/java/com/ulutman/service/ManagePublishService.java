package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishDetailsResponse;
import com.ulutman.model.entities.Publish;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.Category;
import com.ulutman.model.enums.PublishStatus;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.mail.MessagingException;
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
public class ManagePublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final UserRepository userRepository;
    private final MailingService mailingService;

    public List<PublishDetailsResponse> getAllPublish() {
        return publishRepository.findAll().stream().map(publishMapper::mapToDetailsResponse).collect(Collectors.toList());
    }

    public PublishDetailsResponse updatePublishStatus(Long id, PublishStatus newStatus) {
        Publish publish = publishRepository.findById(id).orElseThrow(() ->
                new NotFoundException("Публикация по идентификатору " + id + " не найдена"));

        if (newStatus != null && publish.getPublishStatus() != newStatus) {
            publish.setPublishStatus(newStatus);

            if (newStatus == PublishStatus.ОТКЛОНЕН) {
                log.info("Публикация с id {} отклонена, удаляем публикацию и отправляем уведомление.", id);
                publishRepository.delete(publish);

                try {
                    String userEmail = publish.getUser().getEmail();
                    String publicationTitle = publish.getTitle();
                    mailingService.sendPublicationRejectionNotification(userEmail, publicationTitle);
                } catch (MessagingException e) {
                    log.error("Ошибка при отправке уведомления об отклонении публикации с id {} на email: {}", id, e.getMessage());
                }
            } else {
                publishRepository.save(publish);
            }
        }
        return publishMapper.mapToDetailsResponse(publish);
    }

    public List<PublishDetailsResponse> filterPublishes(List<Category> categories,
                                                        List<PublishStatus> publishStatuses,
                                                        List<LocalDate> createDates,
                                                        String names) {
        List<Publish> filteredPublishes = new ArrayList<>();

        if (categories != null && categories.stream().anyMatch(category -> category == null)) {
            throw new IllegalArgumentException("Категории не могут содержать нулевых значений.");
        } else if (categories != null && !categories.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCategory(categories));
        }

        if (publishStatuses != null && publishStatuses.stream().anyMatch(status -> status == null)) {
            throw new IllegalArgumentException("Статусы публикаций не могут содержать нулевых значений.");
        } else if (publishStatuses != null && !publishStatuses.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByStatus(publishStatuses));
        }

        if (createDates != null && createDates.stream().anyMatch(date -> date == null)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        } else if (createDates != null && !createDates.isEmpty()) {
            filteredPublishes.addAll(publishRepository.filterPublishesByCreateDate(createDates));
        }

        if (names != null && !names.trim().isEmpty()) {
            List<User> filteredUsers = userRepository.findByUserName(names);
            if (filteredUsers.isEmpty()) {
                return Collections.emptyList();
            }

            for (User user : filteredUsers) {
                List<Publish> userPublications = publishRepository.filterPublishesByUserName(user.getName());
                if (!userPublications.isEmpty()) {
                    filteredPublishes.addAll(userPublications);
                }
            }
        }

        filteredPublishes = filteredPublishes.stream().distinct().collect(Collectors.toList());

        if (filteredPublishes.isEmpty()) {
            return Collections.emptyList();
        }

        return filteredPublishes.stream()
                .map(publishMapper::mapToDetailsResponse)
                .collect(Collectors.toList());
    }

    public void deletePublicationsByIds(List<Long> ids) {
        List<Publish> publishes = publishRepository.findAllById(ids);

        if (publishes.isEmpty()) {
            throw new NotFoundException("Публикация с таким ID не найден");
        }

        publishRepository.deleteAll(publishes);
    }

    public void activatePublish(Long publishId) {
        log.info("Активация публикации с ID: {}", publishId);

        Publish publish = publishRepository.findById(publishId)
                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publishId));

        publish.setActive(true);
        publish.setPublishStatus(PublishStatus.ОДОБРЕН); // меняем статус на ОДОБРЕН, если был ОЖИДАЕТ

        publishRepository.save(publish);

        mailingService.sendMailing1(
                publish.getUser().getEmail(),
                "Ваша публикация активирована!",
                "Мы рады сообщить вам, что ваша публикация успешно активирована на сайте ULUTMAN.\n" +
                        "Если у вас есть вопросы, пишите на: ulutmanproject@gmail.com\n\n" +
                        "С уважением,\n" +
                        "Команда ULUTMAN"
        );

        log.info("Публикация активирована и уведомление отправлено пользователю: {}", publish.getUser().getId());
    }
}
