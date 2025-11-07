package raulalvesre.testemeli.application.usecase.dto

data class ProductSearchQuery(
    val filter: ProductFilter,
    val sortOrders: List<SortOrder> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
)
