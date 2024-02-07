package io.rateboard.reservationapi.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("API for reservations streaming")
                                .version("v1")
                )
                .components(new Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme().type(SecurityScheme.Type.APIKEY)
                                .name("Authorization")
                                .in(SecurityScheme.In.HEADER))
                )
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }
}
