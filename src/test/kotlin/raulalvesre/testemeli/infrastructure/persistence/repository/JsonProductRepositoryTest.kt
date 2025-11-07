package raulalvesre.testemeli.infrastructure.persistence.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import raulalvesre.testemeli.application.usecase.dto.ProductFilter
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.application.usecase.dto.SortOrder
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.enums.ProductCondition
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection
import java.math.BigDecimal

class JsonProductRepositoryTest {
    private val products = buildProductList()
    private val repository = JsonProductRepository(products)

    @Test
    fun `findById return product by id`() {
        val result = repository.findById(1L)

        assertNotNull(result)
        assertEquals(result, products[0])
    }

    @Test
    fun `findById returns null when product is not found`() {
        val result = repository.findById(999L)

        assertNull(result)
    }

    @Test
    fun `findPage with empty filter returns all products in first page`() {
        val query =
            ProductSearchQuery(
                filter = ProductFilter(),
                sortOrders = emptyList(),
                page = 0,
                size = 10,
            )

        val result = repository.findPage(query)

        assertEquals(6, result.totalItems)
        assertEquals(6, result.items.size)
        assertEquals(0, result.page)
        assertEquals(10, result.size)

        assertEquals(listOf(1L, 2L, 3L, 4L, 5L, 6L), result.items.map { it.id })
    }

    @Test
    fun `findPage with non-matching filters returns empty results`() {
        val filter =
            ProductFilter(
                name = "MIM",
                brand = "DE",
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders = emptyList(),
                page = 0,
                size = 10,
            )

        val result = repository.findPage(query)

        assertEquals(0, result.totalItems)
        assertTrue(result.items.isEmpty())
    }

    @Test
    fun `findPage filters by name`() {
        val filter =
            ProductFilter(
                name = "iphone",
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders = emptyList(),
                page = 0,
                size = 10,
            )

        val result = repository.findPage(query)

        assertEquals(2, result.totalItems)
        assertEquals(setOf(1L, 3L), result.items.map { it.id }.toSet())
    }

    @Test
    fun `should filter by name and sort by name ASC`() {
        val filter =
            ProductFilter(
                name = "iphone",
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.NAME,
                            direction = SortDirection.ASC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        assertEquals(2, page.totalItems)
        val names = page.items.map { it.name }

        assertEquals(
            listOf("iPhone 14", "iPhone 15"),
            names,
        )
    }

    @Test
    fun `should filter by name and sort by name DESC`() {
        val filter =
            ProductFilter(
                name = "iphone",
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.NAME,
                            direction = SortDirection.DESC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        assertEquals(2, page.totalItems)
        val names = page.items.map { it.name }

        assertEquals(
            listOf("iPhone 15", "iPhone 14"),
            names,
        )
    }

    @Test
    fun `should filter by brand and sort by brand ASC`() {
        val filter =
            ProductFilter(
                brand = "ap",
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.BRAND,
                            direction = SortDirection.ASC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        assertEquals(3, page.totalItems)
        val brands = page.items.map { it.brand }
        val ids = page.items.map { it.id }

        assertEquals(listOf("Apolo", "Apple", "Apple"), brands)

        assertEquals(setOf(1L, 3L, 5L), ids.toSet())
    }

    @Test
    fun `should filter by brand and sort by brand DESC`() {
        val filter =
            ProductFilter(
                brand = "ap",
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.BRAND,
                            direction = SortDirection.DESC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        assertEquals(3, page.totalItems)
        val brands = page.items.map { it.brand }
        val ids = page.items.map { it.id }

        assertEquals(listOf("Apple", "Apple", "Apolo"), brands)
        assertEquals(setOf(1L, 3L, 5L), ids.toSet())
    }

    @Test
    fun `findPage filters by price range and availability`() {
        val filter =
            ProductFilter(
                minPrice = BigDecimal.valueOf(3000.0),
                maxPrice = BigDecimal.valueOf(6000.0),
                isAvailable = true,
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders = emptyList(),
                page = 0,
                size = 10,
            )

        val result = repository.findPage(query)

        assertEquals(3, result.totalItems)
        assertEquals(setOf(1L, 2L, 3L), result.items.map { it.id }.toSet())
    }

    @Test
    fun `findPage sorts by price ascending`() {
        val filter = ProductFilter()
        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(ProductSortField.PRICE, SortDirection.ASC),
                    ),
                page = 0,
                size = 10,
            )

        val result = repository.findPage(query)

        val idsInOrder = result.items.map { it.id }
        assertEquals(listOf(5L, 4L, 6L, 3L, 2L, 1L), idsInOrder)
    }

    @Test
    fun `should filter products by rating range`() {
        val filter =
            ProductFilter(
                minRating = 4.5,
                maxRating = 4.8,
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders = emptyList(),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        assertEquals(2, page.totalItems)
        assertEquals(listOf(1L, 2L), page.items.map { it.id })
    }

    @Test
    fun `should sort by rating ASC with nulls first`() {
        val query =
            ProductSearchQuery(
                filter = ProductFilter(),
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.RATING,
                            direction = SortDirection.ASC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        val idsInOrder = page.items.map { it.id }
        val ratingsInOrder = page.items.map { it.rating }

        assertEquals(listOf(5L, 4L, 3L, 6L, 1L, 2L), idsInOrder)
        assertEquals(listOf(null, 4.1, 4.2, 4.2, 4.8, 4.8), ratingsInOrder)
    }

    @Test
    fun `should sort by rating DESC with nulls last`() {
        val query =
            ProductSearchQuery(
                filter = ProductFilter(),
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.RATING,
                            direction = SortDirection.DESC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        val idsInOrder = page.items.map { it.id }
        val ratingsInOrder = page.items.map { it.rating }

        assertEquals(listOf(1L, 2L, 3L, 6L, 4L, 5L), idsInOrder)
        assertEquals(listOf(4.8, 4.8, 4.2, 4.2, 4.1, null), ratingsInOrder)
    }

    @Test
    fun `findPage sorts by rating desc then name asc`() {
        val filter = ProductFilter()
        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(ProductSortField.RATING, SortDirection.DESC),
                        SortOrder(ProductSortField.NAME, SortDirection.DESC),
                    ),
                page = 0,
                size = 10,
            )

        val result = repository.findPage(query)

        val idsInOrder = result.items.map { it.id }

        assertEquals(listOf(2L, 1L, 6L, 3L, 4L, 5L), idsInOrder)
    }

    @Test
    fun `findPage applies pagination correctly`() {
        val filter = ProductFilter()
        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(ProductSortField.PRICE, SortDirection.ASC),
                    ),
                page = 1,
                size = 2,
            )

        val result = repository.findPage(query)

        assertEquals(6, result.totalItems)
        assertEquals(2, result.items.size)
        assertEquals(listOf(6L, 3L), result.items.map { it.id })
    }

    @Test
    fun `findPage with page out of range returns empty items`() {
        val filter = ProductFilter()
        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders = emptyList(),
                page = 10,
                size = 2,
            )

        val result = repository.findPage(query)

        assertEquals(6, result.totalItems)
        assertTrue(result.items.isEmpty())
    }

    @Test
    fun `should apply multiple filters and sort by price then name`() {
        val filter =
            ProductFilter(
                category = "phones",
                hasFreeShipping = true,
                isAvailable = true,
                minPrice = BigDecimal.valueOf(3000.0),
                maxPrice = BigDecimal.valueOf(7000.0),
            )

        val query =
            ProductSearchQuery(
                filter = filter,
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.PRICE,
                            direction = SortDirection.ASC,
                        ),
                        SortOrder(
                            field = ProductSortField.NAME,
                            direction = SortDirection.DESC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        assertEquals(2, page.totalItems)
        assertEquals(2, page.items.size)
        assertEquals(listOf(3L, 1L), page.items.map { it.id })
    }

    @Test
    fun `should sort by price asc then rating desc when ratings are equal`() {
        val query =
            ProductSearchQuery(
                filter = ProductFilter(),
                sortOrders =
                    listOf(
                        SortOrder(
                            field = ProductSortField.PRICE,
                            direction = SortDirection.ASC,
                        ),
                        SortOrder(
                            field = ProductSortField.RATING,
                            direction = SortDirection.DESC,
                        ),
                    ),
                page = 0,
                size = 10,
            )

        val page = repository.findPage(query)

        val idsInOrder = page.items.map { it.id }

        assertEquals(listOf(5L, 6L, 4L, 3L, 2L, 1L), idsInOrder)
    }

    private fun buildProductList(): List<Product> {
        return listOf(
            Product(
                id = 1,
                name = "iPhone 15",
                price = BigDecimal.valueOf(5999.90),
                brand = "Apple",
                category = "phones",
                condition = ProductCondition.NEW,
                hasFreeShipping = true,
                isAvailable = true,
                rating = 4.8,
                description = "Apple iPhone 15 with 128GB storage and A16 Bionic chip.",
                imageUrl = "https://example.com/images/iphone-15.png",
                specifications =
                    mapOf(
                        Pair("storage", "128GB"),
                    ),
            ),
            Product(
                id = 2,
                name = "Samsung Galaxy S24",
                price = BigDecimal.valueOf(5999.50),
                brand = "Samsung",
                category = "phones",
                condition = ProductCondition.NEW,
                hasFreeShipping = false,
                isAvailable = true,
                rating = 4.8,
                description = "Samsung Galaxy S24 with AMOLED display and 256GB storage.",
                imageUrl = "https://example.com/images/galaxy-s24.png",
                specifications =
                    mapOf(
                        Pair("storage", "256GB"),
                    ),
            ),
            Product(
                id = 3,
                name = "iPhone 14",
                price = BigDecimal.valueOf(3999.90),
                brand = "Apple",
                category = "phones",
                condition = ProductCondition.USED,
                hasFreeShipping = true,
                isAvailable = true,
                rating = 4.2,
                description = "iPhone 14 256GB storage.",
                imageUrl = "https://example.com/images/iphone-14.png",
                specifications =
                    mapOf(
                        Pair("storage", "256GB"),
                    ),
            ),
            Product(
                id = 4,
                name = "Xiaomi Poco",
                price = BigDecimal.valueOf(2499.00),
                brand = "Xiaomi",
                category = "phones",
                condition = ProductCondition.NEW,
                hasFreeShipping = true,
                isAvailable = true,
                rating = 4.1,
                description = "Xiaomi Poco 256GB storage.",
                imageUrl = "https://example.com/images/xiaomi-poco.png",
                specifications =
                    mapOf(
                        Pair("storage", "256GB"),
                    ),
            ),
            Product(
                id = 5,
                name = "Airfry Apolo Air Plus Expert Digital 4,2l Inox UFE2",
                price = BigDecimal.valueOf(472.77),
                brand = "Apolo",
                category = "airfryer",
                condition = ProductCondition.NEW,
                hasFreeShipping = true,
                isAvailable = true,
                rating = null,
                description = "Descubra a solução 2 em 1 para fritar ou grelhar refeições saudáveis em apenas um só produto!",
                imageUrl = "https://example.com/images/airfry-apolo-air-plus-expert.png",
                specifications = mapOf(),
            ),
            Product(
                id = 6,
                name = "Xiaomi Poco 2",
                price = BigDecimal.valueOf(2499.00),
                brand = "Xiaomi",
                category = "phones",
                condition = ProductCondition.NEW,
                hasFreeShipping = true,
                isAvailable = true,
                rating = 4.2,
                description = "Xiaomi Poco 256GB storage.",
                imageUrl = "https://example.com/images/xiaomi-poco.png",
                specifications =
                    mapOf(
                        Pair("storage", "256GB"),
                    ),
            ),
        )
    }
}
