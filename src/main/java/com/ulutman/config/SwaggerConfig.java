package com.ulutman.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.web.filter.ForwardedHeaderFilter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.spi.service.contexts.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.List;
import java.util.Collections;
import java.util.Set;

@Configuration
@SecurityScheme(name = "Authorization",
        description = "JWT auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {

    private static final String CONTROLLER_PACKAGE = "com.example.yourpackage";
    private static final String API_TITLE = "Your API Title";
    private static final String API_DESCRIPTION = "Your API Description";
    private static final String API_VERSION = "1.0";
    private static final String TOKEN_ACCESS_NAME = "Authorization";
    private static final String TOKEN_ACCESS_KEY = "apiKey";
    private static final String TOKEN_ACCESS_TYPE = "header";
    private static final String GLOBAL_SCOPE = "global";
    private static final String ACCESS_EVERYTHING = "accessEverything";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .protocols(Set.of("https"))
                .select()
                .apis(RequestHandlerSelectors.basePackage(CONTROLLER_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(buildApiInfo())
                .securitySchemes(List.of(buildApiKey()))
                .securityContexts(Collections.singletonList(buildSecurityContext()));
    }

    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title(API_TITLE)
                .description(API_DESCRIPTION)
                .version(API_VERSION)
                .build();
    }

    private ApiKey buildApiKey() {
        return new ApiKey(TOKEN_ACCESS_NAME, TOKEN_ACCESS_KEY, TOKEN_ACCESS_TYPE);
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope(GLOBAL_SCOPE, ACCESS_EVERYTHING);
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
        return List.of(new SecurityReference(TOKEN_ACCESS_NAME, authorizationScopes));
    }

    private SecurityContext buildSecurityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .build();
    }

    @Bean
    public ForwardedHeaderFilter forwardedHeaderFilter() {
        return new ForwardedHeaderFilter();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("https://api.ulutman-api.com")
                ));
    }
}

