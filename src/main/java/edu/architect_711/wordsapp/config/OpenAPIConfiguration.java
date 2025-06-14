package edu.architect_711.wordsapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "The Swagger API documentation of the WordsApp", version = "0.0.1"),
        security = @SecurityRequirement(name = "BasciAuth")
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        scheme = "basic",
        name = "BasicAuth"
)
public class OpenAPIConfiguration {

}
