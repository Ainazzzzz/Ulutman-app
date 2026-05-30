package com.ulutman.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping("/api/language")
@Tag(name = "Multi-lingual")
@RequiredArgsConstructor
@Slf4j
public class WelcomeController {

    private final MessageSource messageSource;

    @Operation(
            summary = "Multi-lingual Welcome Message",
            description = "Returns a localized welcome message based on the specified language.",
            parameters = {
                    @Parameter(
                            name = "Accept-Language",
                            description = "Language header for localization (e.g., en, ru, ky, tg). If not provided, defaults to the server's default locale.",
                            required = false,
                            example = "en"
                    )
            }
    )
    @ApiResponse(
            responseCode = "200",
            description = "Successfully returns a localized welcome message.",
            content = @Content(mediaType = "text/plain")
    )
    @GetMapping("/welcome")
    public String welcomeMessage(
            @RequestHeader(name = "Accept-Language", required = false) String acceptLanguage) {
        Locale locale = (acceptLanguage != null) ? Locale.forLanguageTag(acceptLanguage) : LocaleContextHolder.getLocale();
        log.info("Accept-Language header: {}", acceptLanguage);
        log.info("Resolved Locale: {}", locale);
        return messageSource.getMessage("welcome.message", null, locale);
    }
}
