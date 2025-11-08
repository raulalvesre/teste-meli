package raulalvesre.testemeli.infrastructure.api.helper

import raulalvesre.testemeli.domain.enums.ProductCondition
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection

fun parseProductCondition(value: String): ProductCondition {
    val condition =
        ProductCondition
            .entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }

    return condition ?: throw IllegalArgumentException(
        "Invalid product condition: '$value'. " +
            "Available options: ${ProductCondition.entries.joinToString { it.name.lowercase() }}",
    )
}

fun parseProductSortField(value: String?): ProductSortField {
    val sortField =
        ProductSortField
            .entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }

    return sortField ?: throw IllegalArgumentException(
        "Invalid sort field: '$value'. " +
            "Available fields: ${ProductSortField.entries.joinToString { it.name.lowercase() }}",
    )
}

fun parseSortDirection(value: String): SortDirection {
    val condition =
        SortDirection
            .entries
            .firstOrNull { it.name.equals(value, ignoreCase = true) }

    return condition ?: throw IllegalArgumentException(
        "Invalid sort direction: '$value'. " +
            "Must be: ${SortDirection.entries.joinToString { it.name.lowercase() }}",
    )
}
