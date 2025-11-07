package raulalvesre.testemeli.domain.specification

import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.enums.ProductCondition
import java.math.BigDecimal

object ProductTestFixtures {
    fun buildProduct(
        id: Long = 1L,
        name: String = "Test Product",
        description: String = "Test Description",
        price: BigDecimal = BigDecimal("100.00"),
        brand: String? = "Test Brand",
        imageUrl: String = "test.jpg",
        category: String = "Test Category",
        specifications: Map<String, String> = emptyMap(),
        condition: ProductCondition = ProductCondition.NEW,
        rating: Double? = 4.0,
        totalReviews: Int = 0,
        hasFreeShipping: Boolean = false,
        isAvailable: Boolean = true,
    ) = Product(
        id = id,
        name = name,
        description = description,
        price = price,
        brand = brand,
        imageUrl = imageUrl,
        category = category,
        specifications = specifications,
        condition = condition,
        rating = rating,
        totalReviews = totalReviews,
        hasFreeShipping = hasFreeShipping,
        isAvailable = isAvailable,
    )
}
