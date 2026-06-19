package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.MailingMapper;
import com.ulutman.model.dto.MailingRequest;
import com.ulutman.model.dto.MailingResponse;
import com.ulutman.model.dto.UserMailingResponse;
import com.ulutman.model.entities.Mailing;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.MailingStatus;
import com.ulutman.model.enums.MailingType;
import com.ulutman.repository.MailingRepository;
import com.ulutman.repository.UserRepository;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManageMailingService {

    private final MailingRepository mailingRepository;
    private final MailingMapper mailingMapper;
    private final MailingService mailingService;
    private final UserRepository userRepository;

    public MailingResponse createMailingAndSendToAll(
            MailingRequest request
    ) {

        MailingResponse response =
                mailingService.createMailing(request);

        List<User> users = userRepository.findAll();

        for (User user : users) {

            try {

                mailingService.sendMailing(
                        response.getId(),
                        user.getEmail()
                );

            } catch (Exception e) {

                e.printStackTrace();
            }
        }

        return response;

    }


    public List<User> getAllUsersWithMailings() {
        return userRepository.findAllUsersWithMailings();
    }

    public List<MailingResponse> getAllMailings() {
        List<Mailing> mailings = mailingRepository.findAll();

        return mailings.stream()
                .map(this::mapMailingToResponse)
                .collect(Collectors.toList());
    }

    private MailingResponse mapMailingToResponse(Mailing mailing) {
        MailingResponse response = mailingMapper.mapToResponse(mailing);

        List<UserMailingResponse> recipients = Optional.ofNullable(mailing.getRecipients())
                .orElse(Collections.emptyList())
                .stream()
                .map(mailingMapper::mapToUserMailingResponse)
                .collect(Collectors.toList());

        response.setRecipients(recipients);

        return response;
    }

    public MailingResponse updateMailingStatus(Long id, MailingStatus newStatus) {
        Mailing mailing = mailingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mailing not found"));

        if (newStatus != null && mailing.getMailingStatus() != newStatus) {
            mailing.setMailingStatus(newStatus);
            mailingRepository.save(mailing);
        }
        return mailingMapper.mapToResponse(mailing);
    }

    public List<MailingResponse> filterMailingByTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new RuntimeException("Название не может быть пустым или содержать только пробелы.");
        }

        String formattedTitle = title.trim();

        List<Mailing> mailings = mailingRepository.mailingFilterByTitle(formattedTitle);
        return mailings.stream()
                .map(mailingMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<MailingResponse> filterMailing(List<MailingType> mailingTypes,
                                               List<MailingStatus> mailingStatuses,
                                               List<LocalDate> createDates) {

        if (mailingTypes != null && mailingTypes.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Типы рассылок не могут содержать нулевых значений.");
        }

        if (mailingStatuses != null && mailingStatuses.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Статусы рассылок не могут содержать нулевых значений.");
        }

        if (createDates != null && createDates.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("Даты создания не могут содержать нулевых значений.");
        }

        List<Mailing> mailings = mailingRepository.filterMailing(mailingTypes, mailingStatuses, createDates);

        return mailings.stream()
                .map(mailingMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public void deleteMailingsByIds(List<Long> ids) {
        List<Mailing> mailings = mailingRepository.findAllById(ids);

        if (mailings.isEmpty()) {
            throw new NotFoundException("Рассылки с такими ID не найдены");
        }

        mailingRepository.deleteAll(mailings);
    }
}
