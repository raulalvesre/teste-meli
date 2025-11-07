package raulalvesre.testemeli.infrastructure.api.mapper

import raulalvesre.testemeli.application.usecase.dto.ProductFilter
import raulalvesre.testemeli.domain.enums.ProductCondition
import raulalvesre.testemeli.infrastructure.api.request.ProductFilterRequest

object ProductFilterMapper {
    fun ProductFilterRequest.toApplicationFilter(): ProductFilter {
        return ProductFilter(
            name = this.name,
            category = this.category,
            brand = this.brand,
            condition =
                this.condition?.let {
                    ProductCondition.valueOf(it.uppercase())
                },
            minPrice = this.minPrice,
            maxPrice = this.maxPrice,
            minRating = this.minRating,
            maxRating = this.maxRating,
            hasFreeShipping = this.hasFreeShipping,
            isAvailable = this.isAvailable,
        )
    }
}
