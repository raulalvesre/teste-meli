package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct

class AvailabilitySpecificationTest {
    @Test
    fun `should match any product when filter isAvailable is null`() {
        val specification = AvailabilitySpecification(null)

        val availableProduct = buildProduct(isAvailable = true)
        val unavailableProduct = buildProduct(isAvailable = false)

        assertTrue(specification.isSatisfiedBy(availableProduct))
        assertTrue(specification.isSatisfiedBy(unavailableProduct))
    }

    @Test
    fun `should match only available products when filter is true`() {
        val specification = AvailabilitySpecification(true)

        val availableProduct = buildProduct(isAvailable = true)
        val unavailableProduct = buildProduct(isAvailable = false)

        assertTrue(specification.isSatisfiedBy(availableProduct))
        assertFalse(specification.isSatisfiedBy(unavailableProduct))
    }

    @Test
    fun `should match multiple available products`() {
        val specification = AvailabilitySpecification(true)

        val availableProducts =
            listOf(
                buildProduct(isAvailable = true),
                buildProduct(isAvailable = true),
                buildProduct(isAvailable = true),
            )

        val unavailableProducts =
            listOf(
                buildProduct(isAvailable = false),
                buildProduct(isAvailable = false),
            )

        assertTrue(availableProducts.all { specification.isSatisfiedBy(it) })
        assertFalse(unavailableProducts.any { specification.isSatisfiedBy(it) })
    }

    @Test
    fun `should match only unavailable products when filter is false`() {
        val specification = AvailabilitySpecification(false)

        val availableProduct = buildProduct(isAvailable = true)
        val unavailableProduct = buildProduct(isAvailable = false)

        assertFalse(specification.isSatisfiedBy(availableProduct))
        assertTrue(specification.isSatisfiedBy(unavailableProduct))
    }

    @Test
    fun `should match multiple unavailable products`() {
        val specification = AvailabilitySpecification(false)

        val unavailableProducts =
            listOf(
                buildProduct(isAvailable = false),
                buildProduct(isAvailable = false),
                buildProduct(isAvailable = false),
            )

        val availableProducts =
            listOf(
                buildProduct(isAvailable = true),
                buildProduct(isAvailable = true),
            )

        assertTrue(unavailableProducts.all { specification.isSatisfiedBy(it) })
        assertFalse(availableProducts.any { specification.isSatisfiedBy(it) })
    }

    @Test
    fun `should work with AND specification`() {
        val availabilitySpecification = AvailabilitySpecification(true)
        val nameSpec = NameSpecification("Gaming")
        val combinedSpec = availabilitySpecification.and(nameSpec)

        val matchingProduct = buildProduct(name = "Gaming Phone", isAvailable = true)
        val nonMatchingProduct = buildProduct(name = "Gaming Laptop", isAvailable = false)

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val availabilitySpecification = AvailabilitySpecification(true)
        val nameSpec = NameSpecification("Laptop")
        val combinedSpec = availabilitySpecification.or(nameSpec)

        val laptopProduct = buildProduct(name = "Laptop", isAvailable = true)
        val gamingLaptopProduct = buildProduct(name = "Gaming Laptop", isAvailable = false)
        val nonMatchingProduct = buildProduct(name = "T-Shirt", isAvailable = false)

        assertTrue(combinedSpec.isSatisfiedBy(laptopProduct))
        assertTrue(combinedSpec.isSatisfiedBy(gamingLaptopProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val availabilitySpecification = AvailabilitySpecification(true)
        val brandSpec = BrandSpecification("Apple")
        val nameSpec = NameSpecification("Laptop")

        val combinedSpec = availabilitySpecification.and(nameSpec).and(brandSpec)

        val matchingProduct =
            buildProduct(
                name = "Gaming Laptop",
                brand = "Apple",
                isAvailable = true,
            )
        val nonMatchingPrice =
            buildProduct(
                name = "Super Gaming Laptop",
                brand = "Apple",
                isAvailable = false,
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingPrice))
    }
}
