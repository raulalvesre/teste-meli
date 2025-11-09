package raulalvesre.testemeli.infrastructure.api.helper

import raulalvesre.testemeli.application.usecase.dto.SortOrder

fun buildSortOrders(
    sortBy: List<String>?,
    directions: List<String>?,
): List<SortOrder> {
    if (sortBy.isNullOrEmpty()) {
        return emptyList()
    }

    // Mapeia cada campo solicitado para um SortOrder, alinhando a posição dos índices entre a lista
    // de campos e a lista opcional de direções. Direções ausentes assumem ASC para manter o contrato
    // previsível mesmo quando o cliente informa apenas os campos.
    return sortBy.mapIndexed { index, field ->
        val direction = directions?.getOrNull(index) ?: "ASC"
        SortOrder(
            field = parseProductSortField(field.uppercase()),
            direction = parseSortDirection(direction.uppercase()),
        )
    }
}
