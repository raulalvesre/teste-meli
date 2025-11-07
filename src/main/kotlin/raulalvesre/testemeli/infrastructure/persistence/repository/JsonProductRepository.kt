package raulalvesre.testemeli.infrastructure.persistence.repository

import org.springframework.stereotype.Repository
import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.application.usecase.dto.ProductFilter
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.application.usecase.dto.SortOrder
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection
import raulalvesre.testemeli.domain.repository.ProductRepository
import raulalvesre.testemeli.domain.specification.AvailabilitySpecification
import raulalvesre.testemeli.domain.specification.BrandSpecification
import raulalvesre.testemeli.domain.specification.CategorySpecification
import raulalvesre.testemeli.domain.specification.ConditionSpecification
import raulalvesre.testemeli.domain.specification.FreeShippingSpecification
import raulalvesre.testemeli.domain.specification.NameSpecification
import raulalvesre.testemeli.domain.specification.PriceRangeSpecification
import raulalvesre.testemeli.domain.specification.RatingRangeSpecification
import raulalvesre.testemeli.domain.specification.Specification

@Repository
class JsonProductRepository(
    private val products: List<Product> = emptyList(),
) : ProductRepository {
    override fun findById(id: Long): Product? {
        return products.firstOrNull { it.id == id }
    }

    override fun findPage(productSearchQuery: ProductSearchQuery): Page<Product> {
        val specification = productSearchQuery.filter.toSpecification()

        val filteredProducts = products.filter { specification.isSatisfiedBy(it) }
        val sortedFilteredProducts = applySorting(filteredProducts, productSearchQuery.sortOrders)

        val totalItems = sortedFilteredProducts.size
        val page = productSearchQuery.page
        val size = productSearchQuery.size

        val fromIndex = (page * size).coerceAtMost(totalItems)
        val toIndex = (fromIndex + size).coerceAtMost(totalItems)
        val pageItems =
            if (fromIndex <= toIndex) {
                sortedFilteredProducts.subList(fromIndex, toIndex)
            } else {
                emptyList()
            }

        return Page(
            items = pageItems,
            page = page,
            size = size,
            totalItems = totalItems,
        )
    }

    private fun applySorting(
        products: List<Product>,
        sortOrders: List<SortOrder>,
    ): List<Product> {
        if (sortOrders.isEmpty()) {
            return products
        }

        var comparator = buildComparator(sortOrders[0].field, sortOrders[0].direction)

        for (i in 1 until sortOrders.size) {
            val order = sortOrders[i]
            comparator = comparator.thenComparing(buildComparator(order.field, order.direction))
        }

        return products.sortedWith(comparator)
    }

    private fun buildComparator(
        field: ProductSortField,
        direction: SortDirection,
    ): Comparator<Product> {
        val base: Comparator<Product> =
            when (field) {
                ProductSortField.NAME ->
                    compareBy { it.name.lowercase() }

                ProductSortField.PRICE ->
                    compareBy { it.price }

                ProductSortField.RATING ->
                    compareBy { it.rating ?: Double.NEGATIVE_INFINITY }

                ProductSortField.BRAND ->
                    compareBy { it.brand?.lowercase() }
            }

        if (direction == SortDirection.DESC) {
            return base.reversed()
        }

        return base
    }

    private fun ProductFilter.toSpecification(): Specification<Product> {
        return NameSpecification(name)
            .and(CategorySpecification(category))
            .and(BrandSpecification(brand))
            .and(ConditionSpecification(condition))
            .and(PriceRangeSpecification(minPrice, maxPrice))
            .and(RatingRangeSpecification(minRating, maxRating))
            .and(FreeShippingSpecification(hasFreeShipping))
            .and(AvailabilitySpecification(isAvailable))
    }
}
