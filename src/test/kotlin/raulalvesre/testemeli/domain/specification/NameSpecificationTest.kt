package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct
import java.math.BigDecimal

class NameSpecificationTest {
    @Test
    fun `should match any product when filter is null`() {
        val specification = NameSpecification(null)
        val product = buildProduct(name = "Gaming Laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match product with empty name when filter is null`() {
        val specification = NameSpecification(null)
        val product = buildProduct(name = "")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product name contains filter ignoring case`() {
        val specification = NameSpecification("laptop")
        val product = buildProduct(name = "Gaming Laptop Pro")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product name exactly matches filter`() {
        val specification = NameSpecification("Gaming Laptop")
        val product = buildProduct(name = "Gaming Laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product name contains filter as substring`() {
        val specification = NameSpecification("Lap")
        val product = buildProduct(name = "Gaming Laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product name does not contain filter`() {
        val specification = NameSpecification("Laptop")
        val product = buildProduct(name = "Gaming Mouse")

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product name is empty`() {
        val specification = NameSpecification("Laptop")
        val product = buildProduct(name = "")

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should be case insensitive`() {
        val specification = NameSpecification("GAMING LAPTOP")
        val product = buildProduct(name = "gaming laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle mixed case in product name`() {
        val specification = NameSpecification("gaming")
        val product = buildProduct(name = "GAMING Laptop Pro")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match at beginning of product name`() {
        val specification = NameSpecification("Gaming")
        val product = buildProduct(name = "Gaming Laptop Pro")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match at end of product name`() {
        val specification = NameSpecification("Pro")
        val product = buildProduct(name = "Gaming Laptop Pro")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle empty filter name string`() {
        val specification = NameSpecification("")
        val product = buildProduct(name = "Gaming Laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle whitespace in filter name`() {
        val specification = NameSpecification("  Gaming  ")
        val product = buildProduct(name = "Gaming Laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle special characters in name`() {
        val specification = NameSpecification("Gaming-Laptop")
        val product = buildProduct(name = "Gaming-Laptop Pro")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle numbers in name`() {
        val specification = NameSpecification("RTX 3080")
        val product = buildProduct(name = "Gaming Laptop with RTX 3080")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle very long product names`() {
        val longName = "Super Ultra Gaming Laptop Pro Max with RGB Keyboard and High Performance"
        val specification = NameSpecification("Gaming Laptop")
        val product = buildProduct(name = longName)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle unicode characters`() {
        val specification = NameSpecification("café")
        val product = buildProduct(name = "Café Laptop")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should work with AND specification`() {
        val nameSpec = NameSpecification("Laptop")
        val categorySpec = CategorySpecification("Electronics")
        val combinedSpec = nameSpec.and(categorySpec)

        val matchingProduct = buildProduct(name = "Gaming Laptop", category = "Electronics")
        val nonMatchingProduct = buildProduct(name = "Gaming Laptop").copy(category = "Books")

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val nameSpec = NameSpecification("Laptop")
        val categorySpec = CategorySpecification("Books")
        val combinedSpec = nameSpec.or(categorySpec)

        val laptopProduct = buildProduct(name = "Gaming Laptop").copy(category = "Electronics")
        val bookProduct = buildProduct(name = "Science Book").copy(category = "Books")
        val nonMatchingProduct = buildProduct(name = "T-Shirt").copy(category = "Clothing")

        assertTrue(combinedSpec.isSatisfiedBy(laptopProduct))
        assertTrue(combinedSpec.isSatisfiedBy(bookProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val nameSpec = NameSpecification("Laptop")
        val categorySpec = CategorySpecification("Laptop")
        val priceSpec =
            PriceRangeSpecification(
                minPrice = null,
                maxPrice = BigDecimal.valueOf(1500),
            )
        val combinedSpec = nameSpec.and(categorySpec).and(priceSpec)

        val matchingProduct =
            buildProduct(
                name = "Gaming Laptop",
                category = "Laptop",
                price = BigDecimal("1500"),
            )
        val nonMatchingPrice =
            buildProduct(
                name = "Gaming Laptop",
                category = "Laptop",
                price = BigDecimal("3000"),
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingPrice))
    }
}
