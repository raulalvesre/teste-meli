package raulalvesre.testemeli.infrastructure.api.request

import java.math.BigDecimal

data class ProductFilterRequest(
    val name: String? = null,
    val category: String? = null,
    val brand: String? = null,
    val condition: String? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null,
    val minRating: Double? = null,
    val maxRating: Double? = null,
    val hasFreeShipping: Boolean? = null,
    val isAvailable: Boolean? = null,
)
