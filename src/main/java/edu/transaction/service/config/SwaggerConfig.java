package edu.transaction.service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${api.address}")
    private String apiAddress;

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(List.of(new Server().url(apiAddress)))
                .info(new Info().title("Monitor Expense Service API").version("1.0"));
    }
}
