package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct
import java.math.BigDecimal

class PriceRangeSpecificationTest {
    @Test
    fun `should match when price within range`() {
        val spec = PriceRangeSpecification(BigDecimal.valueOf(500.0), BigDecimal.valueOf(1500.0))
        val product = buildProduct(name = "Laptop", price = BigDecimal.valueOf(1000.0), category = "Electronics")

        assertTrue(spec.isSatisfiedBy(product))
    }

    @Test
    fun `should handle null min price`() {
        val spec = PriceRangeSpecification(null, BigDecimal.valueOf(1500.0))
        val product = buildProduct(name = "Laptop", price = BigDecimal.valueOf(2000.0))

        assertFalse(spec.isSatisfiedBy(product))
    }

    @Test
    fun `should handle null max price`() {
        val spec = PriceRangeSpecification(BigDecimal.valueOf(500.0), null)
        val product = buildProduct(name = "Laptop", price = BigDecimal.valueOf(2000.0))

        assertTrue(spec.isSatisfiedBy(product))
    }

    @Test
    fun `should work with AND specification`() {
        val priceRangeSpecification =
            PriceRangeSpecification(
                minPrice = BigDecimal.valueOf(1000L),
                maxPrice = BigDecimal.valueOf(2000L),
            )
        val nameSpec = NameSpecification("Gaming")
        val combinedSpec = priceRangeSpecification.and(nameSpec)

        val matchingProduct = buildProduct(name = "Gaming Laptop", price = BigDecimal.valueOf(1000L))
        val nonMatchingProduct = buildProduct(name = "Gaming Laptop", price = BigDecimal.valueOf(2001L))

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val priceRangeSpecification =
            PriceRangeSpecification(
                minPrice = BigDecimal.valueOf(1000L),
                maxPrice = BigDecimal.valueOf(2000L),
            )
        val nameSpec = NameSpecification("Laptop")
        val combinedSpec = priceRangeSpecification.or(nameSpec)

        val laptopProduct = buildProduct(name = "Gaming Laptop", price = BigDecimal.valueOf(1000L))
        val gamingLaptopProduct = buildProduct(name = "Gaming Laptop", price = BigDecimal.valueOf(9999L))
        val nonMatchingProduct = buildProduct(name = "T-Shirt", brand = "Clothing")

        assertTrue(combinedSpec.isSatisfiedBy(laptopProduct))
        assertTrue(combinedSpec.isSatisfiedBy(gamingLaptopProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val priceSpec =
            PriceRangeSpecification(
                minPrice = null,
                maxPrice = BigDecimal.valueOf(1500),
            )
        val brandSpec = BrandSpecification("Apple")
        val nameSpec = NameSpecification("Laptop")

        val combinedSpec = priceSpec.and(nameSpec).and(brandSpec)

        val matchingProduct =
            buildProduct(
                name = "Gaming Laptop",
                brand = "Apple",
                price = BigDecimal("1500"),
            )
        val nonMatchingPrice =
            buildProduct(
                name = "Gaming Laptop",
                brand = "Electronics",
                price = BigDecimal("3000"),
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingPrice))
    }
}
