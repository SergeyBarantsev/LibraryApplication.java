package com.sber.library.library.project.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI libraryProject() {
        return new OpenAPI()
                .info(new Info()
                        .title("Онлайн библиотека")
                        .description("Сервис, позволяющий арендовать книгу в онлайн библиотеке")
                        .version("v0.1")
                        .license(new License().name("Apache 2.0")
                                .url("http://"))
                        .contact(new Contact()
                                .name("Sergey Barantsev")
                                .email("s.barantsev@mail.ru")));
    }
}