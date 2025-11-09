package raulalvesre.testemeli.domain.repository

import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.pagination.PageResult
import raulalvesre.testemeli.domain.search.ProductSearchQuery

interface ProductRepository {
    fun findById(id: Long): Product?

    fun findPage(productSearchQuery: ProductSearchQuery): PageResult<Product>

    fun findByIds(ids: List<Long>): List<Product>
}
