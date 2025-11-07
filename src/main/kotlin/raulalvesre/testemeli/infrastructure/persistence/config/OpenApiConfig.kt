package raulalvesre.testemeli.infrastructure.persistence.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info =
        Info(
            title = "Teste Meli",
            version = "v1",
            description = "Product search API for the Mercado Livre assessment",
        ),
)
class OpenApiConfig
