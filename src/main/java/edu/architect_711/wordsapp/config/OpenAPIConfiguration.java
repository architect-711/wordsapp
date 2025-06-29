package edu.architect_711.wordsapp.config;

import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import java.util.Collections;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "The Swagger API documentation of the WordsApp", version = "0.0.1"),
        security = @SecurityRequirement(name = "BasicAuth")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        name = "BasicAuth"
)
public class OpenAPIConfiguration {
    @Bean
    public OperationCustomizer removeSecurityForUnsecured() {
        return (operation, _) -> {
            if ("public".equals(operation.getOperationId()))
                operation.setSecurity(Collections.emptyList());

            return operation;
        };
    }
}
