package raulalvesre.testemeli.infrastructure.api.helper

import raulalvesre.testemeli.domain.enums.ProductCondition
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection

fun parseProductCondition(value: String): ProductCondition {
    val condition =
        ProductCondition
            .entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }

    return condition
        ?: throw IllegalArgumentException("condition=$value is invalid")
}

fun parseProductSortField(value: String?): ProductSortField {
    val sortField =
        ProductSortField
            .entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }

    return sortField
        ?: throw IllegalArgumentException("sort field=$value is invalid")
}

fun parseSortDirection(value: String): SortDirection {
    val condition =
        SortDirection
            .entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }

    return condition
        ?: throw IllegalArgumentException("sort direction=$value is invalid")
}
