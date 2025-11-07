package raulalvesre.testemeli.infrastructure.api.response

import raulalvesre.testemeli.domain.enums.ProductCondition
import java.math.BigDecimal

data class ProductResponse(
    val id: Long,
    val title: String,
    val description: String,
    val price: BigDecimal,
    val brand: String?,
    val imageUrl: String,
    val category: String,
    val specifications: Map<String, String>,
    val condition: ProductCondition,
    val rating: Double?,
    val totalReviews: Int,
    val hasFreeShipping: Boolean,
    val isAvailable: Boolean,
)
