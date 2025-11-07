package raulalvesre.testemeli.infrastructure.api.helper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.application.usecase.dto.SortOrder
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection

class BuildSortOrdersTest {
    @Test
    fun `should return empty list when sortBy is null or empty`() {
        assertThat(buildSortOrders(null, null)).isEmpty()
        assertThat(buildSortOrders(emptyList(), null)).isEmpty()
    }

    @Test
    fun `buildSortOrders should map fields and directions when both lists are aligned`() {
        val sortBy =
            listOf(
                ProductSortField.PRICE.name,
                ProductSortField.RATING.name,
                ProductSortField.NAME.name,
            )

        val directions =
            listOf(
                SortDirection.DESC.name,
                SortDirection.ASC.name,
                SortDirection.DESC.name,
            )

        val result = buildSortOrders(sortBy, directions)

        assertThat(result).containsExactly(
            SortOrder(ProductSortField.PRICE, SortDirection.DESC),
            SortOrder(ProductSortField.RATING, SortDirection.ASC),
            SortOrder(ProductSortField.NAME, SortDirection.DESC),
        )
    }

    @Test
    fun `should build orders with default ASC when directions is null`() {
        val orders =
            buildSortOrders(
                sortBy = listOf(ProductSortField.PRICE.name, ProductSortField.RATING.name),
                directions = null,
            )

        assertThat(orders).containsExactly(
            SortOrder(ProductSortField.PRICE, SortDirection.ASC),
            SortOrder(ProductSortField.RATING, SortDirection.ASC),
        )
    }

    @Test
    fun `should use matching directions and default ASC for missing ones`() {
        val orders =
            buildSortOrders(
                sortBy = listOf(ProductSortField.PRICE.name, ProductSortField.RATING.name),
                directions = listOf(SortDirection.DESC.name),
            )

        assertThat(orders).containsExactly(
            SortOrder(ProductSortField.PRICE, SortDirection.DESC),
            SortOrder(ProductSortField.RATING, SortDirection.ASC),
        )
    }
}
