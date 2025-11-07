package raulalvesre.testemeli.infrastructure.api.helper

import raulalvesre.testemeli.application.usecase.dto.SortOrder
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection

fun buildSortOrders(
    sortBy: List<ProductSortField>?,
    directions: List<SortDirection>?,
): List<SortOrder> {
    if (sortBy.isNullOrEmpty()) {
        return emptyList()
    }

    return sortBy.mapIndexed { index, field ->
        val direction = directions?.getOrNull(index) ?: SortDirection.ASC
        SortOrder(
            field = field,
            direction = direction,
        )
    }
}
