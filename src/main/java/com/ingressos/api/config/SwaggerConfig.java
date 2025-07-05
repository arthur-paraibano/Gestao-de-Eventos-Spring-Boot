package com.ingressos.api.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/*
 * Class responsible for managing Swagger which path will open the controller.
 */
@Configuration
public class SwaggerConfig {

        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                                // .components(
                                // new Components()
                                // .addSecuritySchemes(
                                // "bearer",
                                // new SecurityScheme()
                                // .type(SecurityScheme.Type.HTTP)
                                // .scheme("bearer")
                                // .bearerFormat("JWT")
                                // .in(SecurityScheme.In.HEADER)
                                // .name("Authorization")))
                                // .addSecurityItem(new SecurityRequirement().addList("bearer"))
                                .info(new Info()
                                                .title("API de Gestão de Eventos com Vendas de Ingressos")
                                                .version("1.0")
                                                .description("API para gerenciar eventos e vendas de ingressos, com autenticação e autorização"));
        }

        @Bean
        public GroupedOpenApi apiGroup() {
                return GroupedOpenApi.builder()
                                .group("controllers")
                                .packagesToScan("com.ingressos.api.controller")
                                .build();
        }
}