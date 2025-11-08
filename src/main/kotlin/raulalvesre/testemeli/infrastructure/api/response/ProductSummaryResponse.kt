package raulalvesre.testemeli.infrastructure.api.response

import raulalvesre.testemeli.domain.enums.ProductCondition
import java.math.BigDecimal

data class ProductSummaryResponse(
    val id: Long,
    val title: String,
    val price: BigDecimal,
    val imageUrl: String,
    val condition: ProductCondition,
    val rating: Double?,
    val totalReviews: Int,
    val hasFreeShipping: Boolean,
    val isAvailable: Boolean,
)
