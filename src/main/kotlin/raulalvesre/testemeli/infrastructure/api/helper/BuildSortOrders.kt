package raulalvesre.testemeli.infrastructure.api.helper

import raulalvesre.testemeli.application.usecase.dto.SortOrder
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection

fun buildSortOrders(
    sortBy: List<String>?,
    directions: List<String>?,
): List<SortOrder> {
    if (sortBy.isNullOrEmpty()) {
        return emptyList()
    }

    return sortBy.mapIndexed { index, field ->
        val direction = directions?.getOrNull(index) ?: "ASC"
        SortOrder(
            field = ProductSortField.valueOf(field.uppercase()),
            direction = SortDirection.valueOf(direction.uppercase()),
        )
    }
}
