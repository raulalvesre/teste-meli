package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct
import java.math.BigDecimal

class CategorySpecificationTest {
    @Test
    fun `should match any product when filter category is null`() {
        val specification = CategorySpecification(null)
        val product = buildProduct(category = "Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match product with empty category when filter is null`() {
        val specification = CategorySpecification(null)
        val product = buildProduct(category = "")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product category contains filter ignoring case`() {
        val specification = CategorySpecification("electronics")
        val product = buildProduct(category = "Electronics & Computers")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product category exactly matches filter`() {
        val specification = CategorySpecification("Electronics")
        val product = buildProduct(category = "Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product category contains filter as substring`() {
        val specification = CategorySpecification("tronic")
        val product = buildProduct(category = "Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product category does not contain filter`() {
        val specification = CategorySpecification("Electronics")
        val product = buildProduct(category = "Home & Garden")

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product category is empty`() {
        val specification = CategorySpecification("Electronics")
        val product = buildProduct(category = "")

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should be case insensitive`() {
        val specification = CategorySpecification("ELECTRONICS")
        val product = buildProduct(category = "electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle mixed case in product category`() {
        val specification = CategorySpecification("Electronics")
        val product = buildProduct(category = "ELECTRONICS & computers")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle mixed case in product brand`() {
        val specification = CategorySpecification("samsung")
        val product = buildProduct(category = "SAMSUNG Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match at beginning of product brand`() {
        val specification = CategorySpecification("Sam")
        val product = buildProduct(category = "Samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match at end of product brand`() {
        val specification = CategorySpecification("sung")
        val product = buildProduct(category = "Samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle empty filter category string`() {
        val specification = CategorySpecification("")
        val product = buildProduct(category = "Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle whitespace in filter category`() {
        val specification = CategorySpecification("  Electronics  ")
        val product = buildProduct(category = "Home Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle special characters in category`() {
        val specification = CategorySpecification("Home & Garden")
        val product = buildProduct(category = "Home & Garden Supplies")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle very long category names`() {
        val longCategory = "This is a very long category name that might be used in the system"
        val specification = CategorySpecification("long category")
        val product = buildProduct(category = longCategory)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle unicode characters`() {
        val specification = CategorySpecification("café")
        val product = buildProduct(category = "Café Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should work with AND specification`() {
        val categorySpec = CategorySpecification("Electronics")
        val nameSpec = NameSpecification("Laptop")
        val combinedSpec = categorySpec.and(nameSpec)

        val matchingProduct = buildProduct(category = "Electronics", name = "Gaming Laptop")
        val nonMatchingProduct = buildProduct(category = "Electronics", name = "Gaming Mouse")

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val categorySpec = CategorySpecification("Electronics")
        val nameSpec = NameSpecification("Book")
        val combinedSpec = categorySpec.or(nameSpec)

        val electronicsProduct = buildProduct(category = "Electronics", name = "Headphones")
        val bookProduct = buildProduct(name = "Science Book", category = "Books")
        val nonMatchingProduct = buildProduct(name = "T-Shirt", category = "brusinha pai")

        assertTrue(combinedSpec.isSatisfiedBy(electronicsProduct))
        assertTrue(combinedSpec.isSatisfiedBy(bookProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val categorySpec = CategorySpecification("Laptop")
        val nameSpec = NameSpecification("Laptop")
        val priceSpec =
            PriceRangeSpecification(
                minPrice = null,
                maxPrice = BigDecimal.valueOf(1500),
            )
        val combinedSpec = categorySpec.and(nameSpec).and(priceSpec)

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
