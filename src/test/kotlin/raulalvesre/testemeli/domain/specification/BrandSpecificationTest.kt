package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct
import java.math.BigDecimal

class BrandSpecificationTest {
    @Test
    fun `should match any product when filter is null`() {
        val specification = BrandSpecification(null)
        val productWithBrand = buildProduct(brand = "Samsung")
        val productWithNullBrand = buildProduct(brand = null)

        assertTrue(specification.isSatisfiedBy(productWithBrand))
        assertTrue(specification.isSatisfiedBy(productWithNullBrand))
    }

    @Test
    fun `should match when product field contains filter ignoring case`() {
        val specification = BrandSpecification("samsung")
        val product = buildProduct(brand = "Samsung Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product brand exactly matches filter`() {
        val specification = BrandSpecification("Samsung")
        val product = buildProduct(brand = "Samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match when product brand contains filter as substring`() {
        val specification = BrandSpecification("sung")
        val product = buildProduct(brand = "Samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product brand does not contain filter`() {
        val specification = BrandSpecification("Samsung")
        val product = buildProduct(brand = "Apple")

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product brand is null`() {
        val specification = BrandSpecification("Samsung")
        val product = buildProduct(brand = null)

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should not match when product brand is empty`() {
        val specification = BrandSpecification("Samsung")
        val product = buildProduct(brand = "")

        assertFalse(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should be case insensitive`() {
        val specification = BrandSpecification("SAMSUNG")
        val product = buildProduct(brand = "samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle mixed case in product brand`() {
        val specification = BrandSpecification("samsung")
        val product = buildProduct(brand = "SAMSUNG Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match at beginning of product brand`() {
        val specification = BrandSpecification("Sam")
        val product = buildProduct(brand = "Samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should match at end of product brand`() {
        val specification = BrandSpecification("sung")
        val product = buildProduct(brand = "Samsung")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle empty filter brand string`() {
        val specification = BrandSpecification("")
        val productWithBrand = buildProduct(brand = "Samsung")
        val productWithNullBrand = buildProduct(brand = null)

        assertTrue(specification.isSatisfiedBy(productWithBrand))
        assertFalse(specification.isSatisfiedBy(productWithNullBrand))
    }

    @Test
    fun `should handle whitespace in filter brand`() {
        val specification = BrandSpecification("  Samsung  ")
        val product = buildProduct(brand = "Samsung Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle special characters in brand`() {
        val specification = BrandSpecification("H&M")
        val product = buildProduct(brand = "H&M Fashion")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle numbers in brand`() {
        val specification = BrandSpecification("3M")
        val product = buildProduct(brand = "3M Products")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle brands with multiple words`() {
        val specification = BrandSpecification("Hewlett Packard")
        val product = buildProduct(brand = "Hewlett Packard Enterprise")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle very long brand names`() {
        val longBrand = "International Business Machines Corporation"
        val specification = BrandSpecification("Business Machines")
        val product = buildProduct(brand = longBrand)

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should handle unicode characters`() {
        val specification = BrandSpecification("café")
        val product = buildProduct(brand = "Café Electronics")

        assertTrue(specification.isSatisfiedBy(product))
    }

    @Test
    fun `should work with AND specification`() {
        val brandSpec = BrandSpecification("Apple")
        val nameSpec = NameSpecification("Gaming")
        val combinedSpec = brandSpec.and(nameSpec)

        val matchingProduct = buildProduct(name = "Gaming Phone", brand = "Apple")
        val nonMatchingProduct = buildProduct(name = "Gaming Laptop", brand = "Books")

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val brandSpec = BrandSpecification("Apple")
        val nameSpec = NameSpecification("Laptop")
        val combinedSpec = brandSpec.or(nameSpec)

        val laptopProduct = buildProduct(name = "Gaming Laptop", brand = "Electronics")
        val bookProduct = buildProduct(name = "iPhone", brand = "Apple")
        val nonMatchingProduct = buildProduct(name = "T-Shirt", brand = "Clothing")

        assertTrue(combinedSpec.isSatisfiedBy(laptopProduct))
        assertTrue(combinedSpec.isSatisfiedBy(bookProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val brandSpec = BrandSpecification("Apple")
        val nameSpec = NameSpecification("Laptop")
        val priceSpec =
            PriceRangeSpecification(
                minPrice = null,
                maxPrice = BigDecimal.valueOf(1500),
            )
        val combinedSpec = brandSpec.and(nameSpec).and(priceSpec)

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

    @Test
    fun `should not match null brand when filter has value`() {
        val specification = BrandSpecification("Samsung")
        val productWithNullBrand = buildProduct(brand = null)
        val productWithEmptyBrand = buildProduct(brand = "")

        assertFalse(specification.isSatisfiedBy(productWithNullBrand))
        assertFalse(specification.isSatisfiedBy(productWithEmptyBrand))
    }

    @Test
    fun `should match any brand when filter is null`() {
        val specification = BrandSpecification(null)
        val productWithBrand = buildProduct(brand = "Samsung")
        val productWithNullBrand = buildProduct(brand = null)
        val productWithEmptyBrand = buildProduct(brand = "")

        assertTrue(specification.isSatisfiedBy(productWithBrand))
        assertTrue(specification.isSatisfiedBy(productWithNullBrand))
        assertTrue(specification.isSatisfiedBy(productWithEmptyBrand))
    }
}
