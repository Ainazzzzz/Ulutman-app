package com.ulutman.service;

import com.ulutman.model.entities.AdVersiting;
import com.ulutman.model.entities.User;
import com.ulutman.model.enums.MediaFileType;
import com.ulutman.repository.AdVersitingRepository;
import com.ulutman.repository.UserRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import okhttp3.*;
import okhttp3.MediaType;
import okhttp3.RequestBody;

@Service
public class AdVersitingService {

    private final AdVersitingRepository adVersitingRepository;
    private final UserRepository userRepository;
    private final MailingService mailingService;

    @Autowired
    private MinioService minioService;

    // private static final String ADMIN_CHAT_ID = "6640338760";
    // private static final String TELEGRAM_BOT_TOKEN = "7721979760:AAGc8x9AXc5auPzVZX8ajUQjJvXAgNpK6_g";
    private static final String ADMIN_CHAT_ID = "7825590787";
    private static final String TELEGRAM_BOT_TOKEN = "8916468491:AAGZbYzNTZxBaqayYwl5_fYN2wYsa7BAD6s";

    @Autowired
    public AdVersitingService(AdVersitingRepository adVersitingRepository, UserRepository userRepository, MailingService mailingService) throws IOException {
        this.adVersitingRepository = adVersitingRepository;
        this.userRepository = userRepository;
        this.mailingService = mailingService;
    }

    public void createAdvertising(MultipartFile imageFile, String bank, MultipartFile paymentReceiptFile, Principal principal) throws IOException, MessagingException {
        Optional<User> userOptional = userRepository.findByEmail(principal.getName());

        if (!userOptional.isPresent()) {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
        User user = userOptional.get();

        if (imageFile.isEmpty() || paymentReceiptFile.isEmpty()) {
            throw new IllegalArgumentException("Файлы изображения и квитанции не могут быть пустыми.");
        }

        BufferedImage img = ImageIO.read(imageFile.getInputStream());
        if (img == null) {
            throw new IllegalArgumentException("Не удалось прочитать изображение.");
        }

        saveAdvertising(imageFile, bank, paymentReceiptFile, user);
        System.out.println("Реклама создана с изображением: " + imageFile.getOriginalFilename());
    }

    public void saveAdvertising(MultipartFile imageFile, String bank, MultipartFile paymentReceiptFile, User user) throws IOException, MessagingException {
        String userId = String.valueOf(user.getId());

        String imageKey = minioService.upload(imageFile, MediaFileType.AD_IMAGE, userId);
        String receiptKey = minioService.uploadAny(paymentReceiptFile, MediaFileType.AD_RECEIPT, userId);

        String imageUrl = minioService.presign(imageKey);

        AdVersiting ad = new AdVersiting(imageUrl, true, receiptKey, bank, user);
        ad.setCreatedAt(LocalDateTime.now());
        ad.setActive(false);
        adVersitingRepository.save(ad);

        sendReceiptToTelegram(paymentReceiptFile, bank, ad);
    }

    // Оставлен для обратной совместимости с вызовами по email
    public void saveAdvertisingToS3(MultipartFile imageFile, String bank, MultipartFile paymentReceiptFile, String userEmail) throws IOException, MessagingException {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Пользователь с таким email не найден."));
        saveAdvertising(imageFile, bank, paymentReceiptFile, user);
    }

    private void sendReceiptToTelegram(MultipartFile receiptFile, String bankName, AdVersiting adVersiting) {
        OkHttpClient client = new OkHttpClient();

        String messageBody = "Новый чек:\n" +
                "Имя карты: " + bankName + "\n" +
                "Реклама ID: " + adVersiting.getId() + "\n" +
                "Email пользователя: " + (adVersiting.getUser() != null ? adVersiting.getUser().getEmail() : "Не указан");

        try {
            RequestBody fileBody = RequestBody.create(
                    okhttp3.MediaType.parse(receiptFile.getContentType()),
                    receiptFile.getBytes());

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("chat_id", ADMIN_CHAT_ID)
                    .addFormDataPart("document", receiptFile.getOriginalFilename(), fileBody)
                    .addFormDataPart("caption", messageBody)
                    .build();

            Request request = new Request.Builder()
                    .url(String.format("https://api.telegram.org/bot%s/sendDocument", TELEGRAM_BOT_TOKEN))
                    .post(requestBody)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response.code());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при отправке чека в Telegram", e);
        }
    }

    public List<AdVersiting> getAllActiveAds() {
        return adVersitingRepository.findAllActiveAdverting();
    }

    public boolean deleteAd(Long id, Long userId) {
        Optional<AdVersiting> ad = adVersitingRepository.findById(id);
        if (ad.isPresent() && ad.get().getUser().getId().equals(userId)) {
            adVersitingRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
