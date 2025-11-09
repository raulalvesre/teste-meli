package raulalvesre.testemeli.domain.search

import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection

data class SortOrder(
    val field: ProductSortField,
    val direction: SortDirection,
)
