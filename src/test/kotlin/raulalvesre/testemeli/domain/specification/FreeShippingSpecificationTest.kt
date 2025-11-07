package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct

class FreeShippingSpecificationTest {
    @Test
    fun `should match any product when filter hasFreeShipping is null`() {
        val specification = FreeShippingSpecification(null)

        val freeShippingProduct = buildProduct(hasFreeShipping = true)
        val paidShippingProduct = buildProduct(hasFreeShipping = false)

        assertTrue(specification.isSatisfiedBy(freeShippingProduct))
        assertTrue(specification.isSatisfiedBy(paidShippingProduct))
    }

    @Test
    fun `should match only products when free shipping is true`() {
        val specification = FreeShippingSpecification(true)

        val freeShippingProduct = buildProduct(hasFreeShipping = true)
        val paidShippingProduct = buildProduct(hasFreeShipping = false)

        assertTrue(specification.isSatisfiedBy(freeShippingProduct))
        assertFalse(specification.isSatisfiedBy(paidShippingProduct))
    }

    @Test
    fun `should match multiple free shipping products`() {
        val specification = FreeShippingSpecification(true)

        val freeShippingProducts =
            listOf(
                buildProduct(hasFreeShipping = true),
                buildProduct(hasFreeShipping = true),
                buildProduct(hasFreeShipping = true),
            )

        val paidShippingProducts =
            listOf(
                buildProduct(hasFreeShipping = false),
                buildProduct(hasFreeShipping = false),
            )

        assertTrue(freeShippingProducts.all { specification.isSatisfiedBy(it) })
        assertFalse(paidShippingProducts.any { specification.isSatisfiedBy(it) })
    }

    @Test
    fun `should match only paid shipping products when free shipping is false`() {
        val specification = FreeShippingSpecification(false)

        val freeShippingProduct = buildProduct(hasFreeShipping = true)
        val paidShippingProduct = buildProduct(hasFreeShipping = false)

        assertFalse(specification.isSatisfiedBy(freeShippingProduct))
        assertTrue(specification.isSatisfiedBy(paidShippingProduct))
    }

    @Test
    fun `should match multiple paid shipping products`() {
        val specification = FreeShippingSpecification(false)

        val paidShippingProducts =
            listOf(
                buildProduct(hasFreeShipping = false),
                buildProduct(hasFreeShipping = false),
                buildProduct(hasFreeShipping = false),
            )

        val freeShippingProducts =
            listOf(
                buildProduct(hasFreeShipping = true),
                buildProduct(hasFreeShipping = true),
            )

        assertTrue(paidShippingProducts.all { specification.isSatisfiedBy(it) })
        assertFalse(freeShippingProducts.any { specification.isSatisfiedBy(it) })
    }

    @Test
    fun `should work with AND specification`() {
        val freeShippingSpecification = FreeShippingSpecification(true)
        val nameSpec = NameSpecification("Gaming")
        val combinedSpec = freeShippingSpecification.and(nameSpec)

        val matchingProduct = buildProduct(name = "Gaming Phone", hasFreeShipping = true)
        val nonMatchingProduct = buildProduct(name = "Gaming Laptop", hasFreeShipping = false)

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val freeShippingSpecification = FreeShippingSpecification(true)
        val nameSpec = NameSpecification("Laptop")
        val combinedSpec = freeShippingSpecification.or(nameSpec)

        val laptopProduct = buildProduct(name = "Laptop", hasFreeShipping = true)
        val gamingLaptopProduct = buildProduct(name = "Gaming Laptop", hasFreeShipping = false)
        val nonMatchingProduct = buildProduct(name = "T-Shirt", hasFreeShipping = false)

        assertTrue(combinedSpec.isSatisfiedBy(laptopProduct))
        assertTrue(combinedSpec.isSatisfiedBy(gamingLaptopProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val freeShippingSpecification = FreeShippingSpecification(true)
        val brandSpec = BrandSpecification("Apple")
        val nameSpec = NameSpecification("Laptop")

        val combinedSpec = freeShippingSpecification.and(nameSpec).and(brandSpec)

        val matchingProduct =
            buildProduct(
                name = "Gaming Laptop",
                brand = "Apple",
                hasFreeShipping = true,
            )
        val nonMatchingPrice =
            buildProduct(
                name = "Super Gaming Laptop",
                brand = "Apple",
                hasFreeShipping = false,
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingPrice))
    }
}
