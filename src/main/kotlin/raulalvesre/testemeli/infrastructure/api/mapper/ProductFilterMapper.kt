package raulalvesre.testemeli.infrastructure.api.mapper

import raulalvesre.testemeli.domain.search.ProductFilter
import raulalvesre.testemeli.infrastructure.api.helper.parseProductCondition
import raulalvesre.testemeli.infrastructure.api.request.ProductFilterRequest

object ProductFilterMapper {
    fun ProductFilterRequest.toApplicationFilter(): ProductFilter {
        return ProductFilter(
            name = this.name,
            category = this.category,
            brand = this.brand,
            condition =
                this.condition?.let {
                    parseProductCondition(it.uppercase())
                },
            specifications = this.specifications,
            minPrice = this.minPrice,
            maxPrice = this.maxPrice,
            minRating = this.minRating,
            maxRating = this.maxRating,
            hasFreeShipping = this.hasFreeShipping,
            isAvailable = this.isAvailable,
        )
    }
}
