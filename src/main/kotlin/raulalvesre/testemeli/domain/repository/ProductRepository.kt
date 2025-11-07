package raulalvesre.testemeli.domain.repository

import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.domain.entity.Product

interface ProductRepository {
    fun findById(id: Long): Product?

    fun findPage(productSearchQuery: ProductSearchQuery): Page<Product>
}
