package com.ulutman.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class LocaleConfig extends AcceptHeaderLocaleResolver {
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setUseCodeAsDefaultMessage(true);
        return messageSource;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        String acceptLanguage = request.getHeader("Accept-Language");
        System.out.println("Accept-Language: " + acceptLanguage); // Логирование

        List<Locale> supportedLocales = Arrays.asList(
                Locale.forLanguageTag("ru"),
                Locale.forLanguageTag("en"),
                Locale.forLanguageTag("tr"),
                Locale.forLanguageTag("ky"),
                Locale.forLanguageTag("tj"),
                Locale.forLanguageTag("uz")
        );

        if (acceptLanguage == null || acceptLanguage.isEmpty()) {
            return Locale.forLanguageTag("ru");
        }

        Locale requestedLocale = Locale.forLanguageTag(acceptLanguage);
        if (supportedLocales.contains(requestedLocale)) {
            return requestedLocale;
        }

        return Locale.forLanguageTag("ru");
    }
}
