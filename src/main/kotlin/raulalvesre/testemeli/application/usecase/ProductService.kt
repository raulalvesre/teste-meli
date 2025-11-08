package raulalvesre.testemeli.application.usecase

import org.slf4j.LoggerFactory
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

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findById(productId: Long): Product {
        try {
            logger.info("c=ProductService m=findById s=START productId=$productId")
            return productRepository.findById(productId)
                ?: throw ProductNotFoundException(productId)
        } catch (e: Exception) {
            logger.error("c=ProductService m=findById s=ERROR productId=$productId message=${e.message}")
            throw e
        }
    }

    fun findPage(searchQuery: ProductSearchQuery): Page<Product> {
        try {
            logger.info("c=ProductService m=findPage s=START")
            return productRepository.findPage(searchQuery)
        } catch (e: Exception) {
            logger.error("c=ProductService m=findPage s=ERROR searchQuery=$searchQuery message=${e.message}")
            throw e
        }
    }
}
