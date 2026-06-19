package com.ulutman.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
public class FirebaseConfig {

    @PostConstruct
    void init() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream credentials = resolveCredentials();
                if (credentials == null) {
                    log.warn("Firebase credentials not found — Firebase disabled");
                    return;
                }
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(credentials))
                        .build();
                FirebaseApp.initializeApp(options);
                log.info("Firebase initialized successfully");
            }
        } catch (IOException e) {
            log.error("Firebase initialization failed: {}", e.getMessage());
        }
    }

    private InputStream resolveCredentials() {
        // 1. Переменная окружения (Railway, Docker)
        String json = System.getenv("FIREBASE_CREDENTIALS_JSON");
        if (json != null && !json.isBlank()) {
            log.info("Firebase: loading credentials from env variable");
            return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        }

        // 2. Файл в resources (локальная разработка)
        InputStream file = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
        if (file != null) {
            log.info("Firebase: loading credentials from serviceAccountKey.json");
            return file;
        }

        return null;
    }
}
