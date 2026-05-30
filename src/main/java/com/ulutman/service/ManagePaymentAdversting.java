package com.ulutman.service;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.repository.AdVersitingRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ManagePaymentAdversting {

    final MailingService mailingService;
    final AdVersitingRepository adVersitingRepository;

    public List<AdVersiting> getAllDeactivatedPublications() {
        log.info("Получение всех деактивированных рекламы");
        return adVersitingRepository.findAllByActiveFalse();
    }

    public void activatePublication(Long publicationId) {
        log.info("Активация публикации с ID: {}", publicationId);
        AdVersiting publication = adVersitingRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publicationId));

        publication.setActive(true);
        adVersitingRepository.save(publication);
        mailingService.sendMailing1(
                publication.getUser().getEmail(),
                "Ваша реклама активирована!",
                "Мы рады сообщить вам, что ваша реклама по id; " + publication.getId() + " успешно активирована на сайте ULUTMAN.ru \n" +
                        "Если у вас есть вопросы, пишите на: ulutmanproject@gmail.com \n" +
                        "С уважением, " +
                        "Команда ULUTMAN"
        );
        log.info("Публикация активирована и уведомление отправлено пользователю: {}", publication.getUser().getId());
    }

    public void deactivatePublication(Long publicationId) {
        log.info("Деактивация публикации с ID: {}", publicationId);
        AdVersiting publication = adVersitingRepository.findById(publicationId)
                .orElseThrow(() -> new RuntimeException("Публикация не найдена: " + publicationId));

        publication.setActive(false);
        adVersitingRepository.save(publication);
        mailingService.sendMailing1(
                publication.getUser().getEmail(),
                "Уведомление о том, что ваша реклама не активирована",
                "Привет, на связи отдел договоров Ulutman!\n" +
                        "Ваша реклама не активирована по id: " + publication.getId() + ". Возможно, причины связаны с оплатой или с условиями Ulutman.\n" +
                        "Она не будет отображаться на Ulutman.\nСвяжитесь с поддержкой сайта для получения дополнительной информации." +
                        "\nС уважением," +
                        "\nКоманда Ulutman"
        );
        log.info("Публикация деактивирована: {}", publication);
    }
}


