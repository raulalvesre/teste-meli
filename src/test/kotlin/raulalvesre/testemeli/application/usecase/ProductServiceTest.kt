package raulalvesre.testemeli.application.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.application.usecase.dto.ProductFilter
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.exception.ProductNotFoundException
import raulalvesre.testemeli.domain.repository.ProductRepository

class ProductServiceTest {
    private val productRepository: ProductRepository = mockk()
    private val productService = ProductService(productRepository)

    @Test
    fun `findById should return product when it exists`() {
        val productId = 1L
        val product = mockk<Product>()
        every { productRepository.findById(productId) } returns product

        val result = productService.findById(productId)

        assertThat(result).isEqualTo(product)
        verify(exactly = 1) { productRepository.findById(productId) }
    }

    @Test
    fun `findById should throw ProductNotFoundException when product does not exist`() {
        val productId = 999L
        every { productRepository.findById(productId) } returns null

        assertThrows(ProductNotFoundException::class.java) {
            productService.findById(productId)
        }

        verify(exactly = 1) { productRepository.findById(productId) }
    }

    @Test
    fun `findPage should delegate to repository and return its result`() {
        val query =
            ProductSearchQuery(
                filter = ProductFilter(),
                page = 0,
                size = 10,
                sortOrders = emptyList(),
            )

        val pageResult = mockk<Page<Product>>()
        every { productRepository.findPage(query) } returns pageResult

        val result = productService.findPage(query)

        assertThat(result).isEqualTo(pageResult)
        verify(exactly = 1) { productRepository.findPage(query) }
    }
}
