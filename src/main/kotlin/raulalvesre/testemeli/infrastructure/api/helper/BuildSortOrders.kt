package raulalvesre.testemeli.infrastructure.api.helper

import raulalvesre.testemeli.application.usecase.dto.SortOrder

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
            field = parseProductSortField(field.uppercase()),
            direction = parseSortDirection(direction.uppercase()),
        )
    }
}
