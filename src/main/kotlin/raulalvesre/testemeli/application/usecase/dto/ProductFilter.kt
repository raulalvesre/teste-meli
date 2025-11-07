package raulalvesre.testemeli.application.usecase.dto

import raulalvesre.testemeli.domain.enums.ProductCondition
import java.math.BigDecimal

data class ProductFilter(
    val name: String? = null,
    val category: String? = null,
    val brand: String? = null,
    val condition: ProductCondition? = null,
    val minPrice: BigDecimal? = null,
    val maxPrice: BigDecimal? = null,
    val minRating: Double? = null,
    val maxRating: Double? = null,
    val hasFreeShipping: Boolean? = null,
    val isAvailable: Boolean? = null,
)
