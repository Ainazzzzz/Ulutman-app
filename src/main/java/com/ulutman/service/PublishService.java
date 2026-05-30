package com.ulutman.service;

import com.ulutman.exception.NotFoundException;
import com.ulutman.mapper.ConditionsMapper;
import com.ulutman.mapper.PropertyDetailsMapper;
import com.ulutman.mapper.PublishMapper;
import com.ulutman.model.dto.PublishRequest;
import com.ulutman.model.dto.PublishResponse;
import com.ulutman.model.entities.*;
import com.ulutman.model.enums.*;
import com.ulutman.repository.FavoriteRepository;
import com.ulutman.repository.MyPublishRepository;
import com.ulutman.repository.PublishRepository;
import com.ulutman.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PublishService {

    private final PublishMapper publishMapper;
    private final PublishRepository publishRepository;
    private final UserRepository userRepository;
    private final PropertyDetailsMapper propertyDetailsMapper;
    private final ConditionsMapper conditionsMapper;
    private final MailingService mailingService;
    private final FavoriteRepository favoriteRepository;


    private static final String ADMIN_CHAT_ID = "6640338760";
    private static final String TELEGRAM_BOT_TOKEN = "7721979760:AAGc8x9AXc5auPzVZX8ajUQjJvXAgNpK6_g";
    private final MyPublishRepository myPublishRepository;


    public PublishResponse createPublish(PublishRequest publishRequest) {
        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
        }


        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
            throw new IllegalArgumentException("Неверная категория");
        }


        if (!publishRequest.getCategory().getSubcategories().contains(publishRequest.getSubcategory())) {
            throw new IllegalArgumentException("Неверная подкатегория для выбранной категории");
        }

        List<String> imageUrls = publishRequest.getImages();
        log.info("URL изображений: {}", imageUrls);


        for (String url : imageUrls) {
            if (!url.startsWith("http")) {
                throw new IllegalArgumentException("Некорректный URL изображения: " + url);
            }
        }
        Publish publish = publishMapper.mapToEntity(publishRequest);
        publish.setCreatedAt(LocalDateTime.now());
        User user = userRepository.findById(publishRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден: " + publishRequest.getUserId()));
        if (publish.getMetro() != null) {
            String metroName = formatMetroName(publish.getMetro());
            publish.setMetroStation(metroName);
        }
        publish.setUser(user);
        publish.setImages(imageUrls);

        if (publishRequest.getCategory() == Category.HOTEL || publishRequest.getCategory() == Category.RENT || publishRequest.getCategory() == Category.REAL_ESTATE) {
            publish.setPublishStatus(PublishStatus.ОЖИДАЕТ);
        } else {
            publish.setPublishStatus(PublishStatus.ОДОБРЕН);
        }
        publish.setCategoryStatus(CategoryStatus.АКТИВНО);


        if (publishRequest.getCategory() == Category.RENT || publishRequest.getCategory() == Category.HOTEL || publishRequest.getCategory() == Category.REAL_ESTATE) {
            publish.setActive(false);
        } else {
            publish.setActive(true);
        }

        Publish savedPublish;
        try {
            savedPublish = publishRepository.save(publish);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Ошибка при сохранении публикации: " + e.getMessage());
        }


        if (publishRequest.getCategory() == Category.RENT || publishRequest.getCategory() == Category.HOTEL || publishRequest.getCategory() == Category.REAL_ESTATE) {
            String bankName = publishRequest.getBank()
                    .orElseThrow(() -> new IllegalArgumentException("Необходимо выбрать банк для категории " + publishRequest.getCategory()));
            MultipartFile receiptFile = (MultipartFile) publishRequest.getPaymentReceiptFile()
                    .orElseThrow(() -> new IllegalArgumentException("Необходимо предоставить чек оплаты для категории " + publishRequest.getCategory()));

            try {
                sendReceiptAsDocumentToTelegram(receiptFile, bankName, savedPublish); // Передаем MultipartFile напрямую
            } catch (IOException e) {
                log.error("Ошибка при отправке чека в Telegram: {}", e.getMessage(), e);
                throw new RuntimeException("Ошибка при отправке чека", e);
            }
        } else {
            log.info("Bank and payment receipt are not required for category: {}", publishRequest.getCategory());
        }


        MyPublish myPublish = new MyPublish();
        myPublish.setUserAccount(user.getUserAccount());
        myPublish.setPublish(savedPublish);

        myPublishRepository.save(myPublish);

        log.info("Publication created successfully: {}", savedPublish);

        return publishMapper.mapToResponse(savedPublish);
    }

    public static String formatMetroName(Metro metro) {
        String name = metro.name();
        StringBuilder formattedName = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (i > 0 && Character.isUpperCase(c)) {
                formattedName.append(" ");
            }
            formattedName.append(c);
        }
        return formattedName.toString();
    }

    @Scheduled(fixedRate = 86400000)
    public void scheduleExpiredPublishesRemoval() {
        removeExpiredPublishes();
    }

    private void sendReceiptAsDocumentToTelegram(MultipartFile receiptFile, String bankName, Publish savedPublish) throws IOException {
        String userEmail = savedPublish.getUser().getEmail();
        String userPhoneNumber = savedPublish.getPhone();

        String message = String.format(
                "Новый чек:\nИмя карты: %s\nПубликация ID: %s\nEmail пользователя: %s\nНомер телефона: %s",
                bankName, savedPublish.getId(), userEmail != null ? userEmail : "Не указан", userPhoneNumber != null ? userPhoneNumber : "Не указан"
        );

        OkHttpClient client = new OkHttpClient();
        String url = String.format("https://api.telegram.org/bot%s/sendDocument", TELEGRAM_BOT_TOKEN);

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("chat_id", ADMIN_CHAT_ID)
                .addFormDataPart("caption", message)
                .addFormDataPart("document", receiptFile.getOriginalFilename(),
                        RequestBody.create(receiptFile.getBytes(), MediaType.parse(receiptFile.getContentType()))); // Используем

        RequestBody requestBody = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) { // try-with-resources для автоматического закрытия ответа
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
        }
    }

    @Transactional
    public void removeExpiredPublishes() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expirationTime = now.minusDays(30);// 30 дней назад


        List<Publish> expiredPublishes = publishRepository.findAllByCreatedAtBefore(expirationTime);

        for (Publish publish : expiredPublishes) {
            publishRepository.delete(publish);
            mailingService.sendMailing1(
                    publish.getUser().getEmail(),
                    "Уведомление о завершении срока действия",
                    "Привет, на связи отдел договоров Ulutman.ru!\n" +
                            "Срок действия вашего объявления: {" + publish + "} подошел к концу. \n" +
                            " Оно больше не будет отображаться на Ulutman.ru.\n" +
                            " С уважением," +
                            " Команда Ulutman.ru");
        }
    }

    public PublishResponse createPublishDetails(PublishRequest publishRequest) {


        if (publishRequest.getCategory() == null || publishRequest.getSubcategory() == null) {
            throw new IllegalArgumentException("Необходимо выбрать категорию и подкатегорию");
        }

        if (!Category.getAllSubcategories(publishRequest.getCategory()).contains(publishRequest.getSubcategory())) {
            throw new IllegalArgumentException("Неверная подкатегория для выбранной категории");
        }

        if (!Category.getAllCategories().contains(publishRequest.getCategory())) {
            throw new IllegalArgumentException("Неверная категория");
        }

        Publish publish = publishMapper.mapToEntity(publishRequest);
        User user = userRepository.findById(publishRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("Пользователь не найден " + publishRequest.getUserId()));
        publish.setUser(user);

        if (publish.getId() == null) {
            publish.setCreateDate(LocalDate.now());
        }

        publish.setPublishStatus(PublishStatus.ОДОБРЕН);
        publish.setCategoryStatus(CategoryStatus.АКТИВНО);

        if (publishRequest.getCategory() == Category.REAL_ESTATE || publishRequest.getCategory() == Category.RENT) {
            if (publishRequest.getPropertyDetails() == null) {
                throw new IllegalArgumentException("Необходимо заполнить данные о недвижимости (PropertyDetails) для категории REAL_ESTATE или RENT.");
            }
            PropertyDetails propertyDetails = propertyDetailsMapper.mapToEntity(publishRequest.getPropertyDetails());
            publish.setPropertyDetails(propertyDetails);
        }

        if (publishRequest.getConditions() == null) {
            throw new IllegalArgumentException("Необходимо заполнить данные о условиях (Conditions) для категории REAL_ESTATE или RENT.");
        }
        Conditions conditions = conditionsMapper.mapToEntity(publishRequest.getConditions());
        publish.setConditions(conditions);

        Publish savedPublish = publishRepository.save(publish);

        return publishMapper.mapToResponse(savedPublish);
    }

    public Integer getNumberOfPublications(Long userId) {
        return publishRepository.countPublicationsByUserId(userId);
    }

    public PublishResponse findById(Long id, Principal principal) {
        Publish publish = publishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Публикация по идентификатору " + id + " не найдена"));


        if (principal != null) {
            User user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new NotFoundException("Пользователь не найден"));


            Favorite favorites = favoriteRepository.getFavoritesByUserId(user.getId());
            if (favorites != null && favorites.getPublishes().contains(publish)) {
                publish.setDetailFavorite(true);
            } else {
                publish.setDetailFavorite(false);
            }
        }


        return publishMapper.mapToResponse(publish);
    }

//    public PublishResponse findById(Long id) {
//        Publish publish = publishRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Публикация по идентификатору " + id + " не найдена"));
//        return publishMapper.mapToResponse(publish);
//    }

    public PublishResponse updatePublish(Long id, PublishRequest publishRequest) {
        Publish existingPublish = publishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Публикация не найдена по идентификатору: " + id));
        existingPublish.setDescription(publishRequest.getDescription());
        existingPublish.setMetro(publishRequest.getMetro());
        existingPublish.setAddress(publishRequest.getAddress());
        existingPublish.setCategory(publishRequest.getCategory());
        existingPublish.setSubCategory(publishRequest.getSubcategory());
        publishRepository.save(existingPublish);
        return publishMapper.mapToResponse(existingPublish);
    }

    public void deletePublish(Long productId) {
        this.publishRepository.findById(productId).orElseThrow(() -> {
            return new EntityNotFoundException("Публикация  по идентификатору " + productId + " успешно удалено");
        });
        this.publishRepository.deleteById(productId);
    }

    public List<PublishResponse> getAll(Principal principal) {
        List<Publish> publishes = publishRepository.findAllActivePublishes();

        // Проверяем, есть ли текущий пользователь
        boolean userIsLoggedIn = principal != null;
        Long userId = userIsLoggedIn ? userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")).getId() : null;

        return publishes.stream()
                .map(publish -> {
                    PublishResponse publishResponse = publishMapper.mapToResponse(publish);

                    if (userIsLoggedIn) {
                        // Если пользователь зарегистрирован, проверяем, есть ли публикация в избранном
                        Favorite favorites = favoriteRepository.getFavoritesByUserId(userId);
                        boolean isFavorite = favorites != null && favorites.getPublishes().contains(publish);
                        publishResponse.setDetailFavorite(isFavorite); // Устанавливаем значение в зависимости от наличия
                    } else {
                        // Если пользователь не зарегистрирован, устанавливаем detailFavorite в false
                        publishResponse.setDetailFavorite(false);
                    }

                    return publishResponse;
                })
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public List<PublishResponse> filterPublishes(
            Double minTotalArea,
            Double maxTotalArea,
            Double minKitchenArea,
            Double maxKitchenArea,
            Double minLivingArea,
            Double maxLivingArea,
            Integer minYear,
            Integer maxYear,
            TransportType transportType,
            Double walkingDistance,
            Double transportDistance
    ) {
        List<Publish> publishes = publishRepository.filterPublishes(
                minTotalArea,
                maxTotalArea,
                minKitchenArea,
                maxKitchenArea,
                minLivingArea,
                maxLivingArea,
                minYear,
                maxYear,
                transportType,
                walkingDistance,
                transportDistance
        );

        return publishes.stream()
                .map(publishMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}