package raulalvesre.testemeli.domain.search

data class ProductSearchQuery(
    val filter: ProductFilter,
    val sortOrders: List<SortOrder> = emptyList(),
    val page: Int = 0,
    val size: Int = 10,
)
