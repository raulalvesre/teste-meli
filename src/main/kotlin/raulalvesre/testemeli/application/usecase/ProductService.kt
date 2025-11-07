package raulalvesre.testemeli.application.usecase

import org.springframework.stereotype.Service
import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.exception.ProductNotFoundException
import raulalvesre.testemeli.domain.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
) {
    fun findById(productId: Long): Product {
        return productRepository.findById(productId)
            ?: throw ProductNotFoundException(productId)
    }

    fun findPage(searchQuery: ProductSearchQuery): Page<Product> {
        return productRepository.findPage(searchQuery)
    }
}
