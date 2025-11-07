package raulalvesre.testemeli.infrastructure.persistence.config

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.repository.ProductRepository
import raulalvesre.testemeli.infrastructure.persistence.repository.JsonProductRepository

@Configuration
class RepositoryConfig(
    private val objectMapper: ObjectMapper,
) {
    @Bean
    fun productRepository(): ProductRepository {
        val inputStream =
            javaClass.getResourceAsStream("/data/products.json")
                ?: throw IllegalStateException("products.json not found on classpath")

        val products: List<Product> =
            inputStream.use {
                objectMapper.readValue(it, object : TypeReference<List<Product>>() {})
            }

        return JsonProductRepository(products)
    }
}
