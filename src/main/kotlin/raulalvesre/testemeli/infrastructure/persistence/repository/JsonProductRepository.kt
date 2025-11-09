package raulalvesre.testemeli.infrastructure.persistence.repository

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection
import raulalvesre.testemeli.domain.pagination.PageResult
import raulalvesre.testemeli.domain.repository.ProductRepository
import raulalvesre.testemeli.domain.search.ProductFilter
import raulalvesre.testemeli.domain.search.ProductSearchQuery
import raulalvesre.testemeli.domain.search.SortOrder
import raulalvesre.testemeli.domain.specification.AvailabilitySpecification
import raulalvesre.testemeli.domain.specification.BrandSpecification
import raulalvesre.testemeli.domain.specification.CategorySpecification
import raulalvesre.testemeli.domain.specification.ConditionSpecification
import raulalvesre.testemeli.domain.specification.FreeShippingSpecification
import raulalvesre.testemeli.domain.specification.NameSpecification
import raulalvesre.testemeli.domain.specification.PriceRangeSpecification
import raulalvesre.testemeli.domain.specification.RatingRangeSpecification
import raulalvesre.testemeli.domain.specification.Specification
import raulalvesre.testemeli.domain.specification.SpecificationsSpecification

@Repository
class JsonProductRepository(
    private val products: List<Product> = emptyList(),
) : ProductRepository {
    private val logger = LoggerFactory.getLogger(this::class.java)

    override fun findById(id: Long): Product? {
        try {
            logger.info("c=JsonProductRepository m=findById s=START productId=$id")
            val products = products.firstOrNull { it.id == id }
            logger.info("c=JsonProductRepository m=findById s=DONE")
            return products
        } catch (e: Exception) {
            logger.error("c=JsonProductRepository m=findById s=ERROR productId=$id message=${e.message}", e)
            throw e
        }
    }

    override fun findByIds(ids: List<Long>): List<Product> {
        try {
            logger.info("c=JsonProductRepository m=findByIds s=START productIds=$ids")
            val products = products.filter { it.id in ids }
            logger.info("c=JsonProductRepository m=findByIds s=DONE")
            return products
        } catch (e: Exception) {
            logger.error("c=JsonProductRepository m=findByIds s=ERROR productIds=$ids message=${e.message}", e)
            throw e
        }
    }

    override fun findPage(productSearchQuery: ProductSearchQuery): PageResult<Product> {
        try {
            logger.info("c=JsonProductRepository m=findPage s=START")
            val specification = productSearchQuery.filter.toSpecification()

            val filteredProducts = products.filter { specification.isSatisfiedBy(it) }
            val sortedFilteredProducts = applySorting(filteredProducts, productSearchQuery.sortOrders)

            val totalItems = sortedFilteredProducts.size
            val page = productSearchQuery.page
            val size = productSearchQuery.size

            // Calculate pagination bounds - ensure we don't exceed list boundaries
            val fromIndex = (page * size).coerceAtMost(totalItems)
            val toIndex = (fromIndex + size).coerceAtMost(totalItems)

            val pageItems =
                if (fromIndex <= toIndex) {
                    sortedFilteredProducts.subList(fromIndex, toIndex)
                } else {
                    emptyList()
                }

            logger.info("c=JsonProductRepository m=findPage s=DONE")

            return PageResult(
                items = pageItems,
                page = page,
                size = size,
                totalItems = totalItems,
            )
        } catch (e: Exception) {
            logger.error("c=JsonProductRepository m=findPage s=ERROR message=${e.message}", e)
            throw e
        }
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

    // Constrói uma única especificação composta encadeando cada filtro opcional com AND lógico.
    // Cada especificação lida com valores nulos, então filtros ausentes atuam como no-ops e
    // mantêm a legibilidade da combinação declarada aqui.
    private fun ProductFilter.toSpecification(): Specification<Product> {
        return NameSpecification(name)
            .and(CategorySpecification(category))
            .and(BrandSpecification(brand))
            .and(ConditionSpecification(condition))
            .and(SpecificationsSpecification(specifications))
            .and(PriceRangeSpecification(minPrice, maxPrice))
            .and(RatingRangeSpecification(minRating, maxRating))
            .and(FreeShippingSpecification(hasFreeShipping))
            .and(AvailabilitySpecification(isAvailable))
    }
}
