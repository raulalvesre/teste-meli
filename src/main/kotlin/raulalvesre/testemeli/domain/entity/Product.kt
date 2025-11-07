package raulalvesre.testemeli.domain.entity

import raulalvesre.testemeli.domain.enums.ProductCondition
import java.math.BigDecimal

data class Product(
    val id: Long,
    val name: String,
    val description: String,
    val price: BigDecimal,
    val brand: String?,
    val imageUrl: String,
    val category: String,
    val specifications: Map<String, String>,
    val condition: ProductCondition,
    val rating: Double? = null,
    val totalReviews: Int = 0,
    val hasFreeShipping: Boolean = false,
    val isAvailable: Boolean = true,
)
