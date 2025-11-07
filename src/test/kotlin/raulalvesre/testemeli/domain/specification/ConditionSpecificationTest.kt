package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.enums.ProductCondition
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct
import java.math.BigDecimal

class ConditionSpecificationTest {
    @Test
    fun `should match any product when filter condition is null`() {
        val specification = ConditionSpecification(null)

        val newProduct = buildProduct(condition = ProductCondition.NEW)
        val usedProduct = buildProduct(condition = ProductCondition.USED)
        val refurbishedProduct = buildProduct(condition = ProductCondition.REFURBISHED)
        val openBoxProduct = buildProduct(condition = ProductCondition.OPEN_BOX)

        assertTrue(specification.isSatisfiedBy(newProduct))
        assertTrue(specification.isSatisfiedBy(usedProduct))
        assertTrue(specification.isSatisfiedBy(refurbishedProduct))
        assertTrue(specification.isSatisfiedBy(openBoxProduct))
    }

    @Test
    fun `should match when product condition exactly matches NEW filter`() {
        val specification = ConditionSpecification(ProductCondition.NEW)
        val product = buildProduct(condition = ProductCondition.NEW)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product condition exactly matches USED filter`() {
        val specification = ConditionSpecification(ProductCondition.USED)
        val product = buildProduct(condition = ProductCondition.USED)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product condition exactly matches REFURBISHED filter`() {
        val specification = ConditionSpecification(ProductCondition.REFURBISHED)
        val product = buildProduct(condition = ProductCondition.REFURBISHED)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product condition exactly matches OPEN_BOX filter`() {
        val specification = ConditionSpecification(ProductCondition.OPEN_BOX)
        val product = buildProduct(condition = ProductCondition.OPEN_BOX)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product condition differs from filter`() {
        val specification = ConditionSpecification(ProductCondition.NEW)
        val usedProduct = buildProduct(condition = ProductCondition.USED)
        val refurbishedProduct = buildProduct(condition = ProductCondition.REFURBISHED)
        val openBoxProduct = buildProduct(condition = ProductCondition.OPEN_BOX)

        assertFalse(specification.isSatisfiedBy(usedProduct))
        assertFalse(specification.isSatisfiedBy(refurbishedProduct))
        assertFalse(specification.isSatisfiedBy(openBoxProduct))
    }

    @Test
    fun `should not match when filter is USED and product is NEW`() {
        val specification = ConditionSpecification(ProductCondition.USED)
        val product = buildProduct(condition = ProductCondition.NEW)

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should correctly match all condition pairs`() {
        val allConditions = ProductCondition.entries.toTypedArray()

        for (filterCondition in allConditions) {
            val specification = ConditionSpecification(filterCondition)

            for (productCondition in allConditions) {
                val product = buildProduct(condition = productCondition)
                val expected = filterCondition == productCondition

                assertEquals(
                    expected,
                    specification.isSatisfiedBy(product),
                )
            }
        }
    }

    @Test
    fun `should work with AND specification`() {
        val conditionSpec = ConditionSpecification(ProductCondition.NEW)
        val brandSpec = BrandSpecification("Samsung")
        val combinedSpec = conditionSpec.and(brandSpec)

        val matchingProduct = buildProduct(condition = ProductCondition.NEW, brand = "Samsung")
        val nonMatchingCondition = buildProduct(condition = ProductCondition.USED, brand = "Samsung")
        val nonMatchingBrand = buildProduct(condition = ProductCondition.NEW, brand = "Apple")

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingCondition))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingBrand))
    }

    @Test
    fun `should work with OR specification`() {
        val conditionSpec = ConditionSpecification(ProductCondition.NEW)
        val freeShippingSpec = FreeShippingSpecification(true)
        val combinedSpec = conditionSpec.or(freeShippingSpec)

        val newProduct = buildProduct(condition = ProductCondition.NEW, hasFreeShipping = false)
        val usedFreeShipping = buildProduct(condition = ProductCondition.USED, hasFreeShipping = true)
        val usedPaidShipping = buildProduct(condition = ProductCondition.USED, hasFreeShipping = false)

        assertTrue(combinedSpec.isSatisfiedBy(newProduct))
        assertTrue(combinedSpec.isSatisfiedBy(usedFreeShipping))
        assertFalse(combinedSpec.isSatisfiedBy(usedPaidShipping))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val conditionSpec = ConditionSpecification(ProductCondition.REFURBISHED)
        val categorySpec = CategorySpecification("Electronics")
        val priceSpec = PriceRangeSpecification(BigDecimal("200"), BigDecimal("1000"))
        val combinedSpec = conditionSpec.and(categorySpec).and(priceSpec)

        val matchingProduct =
            buildProduct(
                condition = ProductCondition.REFURBISHED,
                category = "Electronics",
                price = BigDecimal("500"),
            )
        val nonMatchingCondition =
            buildProduct(
                condition = ProductCondition.NEW,
                category = "Electronics",
                price = BigDecimal("500"),
            )
        val nonMatchingCategory =
            buildProduct(
                condition = ProductCondition.REFURBISHED,
                category = "Books",
                price = BigDecimal("500"),
            )
        val nonMatchingPrice =
            buildProduct(
                condition = ProductCondition.REFURBISHED,
                category = "Electronics",
                price = BigDecimal("1500"),
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingCondition))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingCategory))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingPrice))
    }
}
