package com.program.lms.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI lmsOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("LMS API")
                        .description("REST API for managing courses, groups, students, teachers," +
                                " and lesson schedules within the LMS platform.")
                        .version("1.0.0")
                )
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local environment")
                ));
    }
}
