package raulalvesre.testemeli.domain.pagination

data class PageResult<T>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalItems: Int,
) {
    val totalPages: Int
        get() =
            if (size == 0) {
                0
            } else {
                ((totalItems + size - 1) / size)
            }

    val isFirst: Boolean
        get() = page <= 0

    val isLast: Boolean
        get() = page + 1 >= totalPages
}
