package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct

class SpecificationsSpecificationTest {
    @Test
    fun `should return true when required specifications is null`() {
        val specification = SpecificationsSpecification(null)
        val product =
            buildProduct(
                specifications = mapOf("color" to "red", "size" to "large"),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should return true when required specifications is empty`() {
        val specification = SpecificationsSpecification(emptyMap())
        val product =
            buildProduct(
                specifications = mapOf("color" to "red", "size" to "large"),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should return true when product has all required specifications with exact match`() {
        val requiredSpecs =
            mapOf(
                "color" to "red",
                "size" to "large",
                "material" to "cotton",
            )
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications =
                    mapOf(
                        "color" to "red",
                        "size" to "large",
                        "material" to "cotton",
                        "brand" to "nike",
                    ),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should return true when product specifications contain required values with different case`() {
        val requiredSpecs =
            mapOf(
                "color" to "RED",
                "size" to "Large",
            )
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications =
                    mapOf(
                        "color" to "red",
                        "size" to "LARGE",
                    ),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should return true when product specification value contains required value as substring`() {
        val requiredSpecs =
            mapOf(
                "model" to "iPhone",
                "storage" to "128GB",
            )
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications =
                    mapOf(
                        "model" to "iPhone 15 Pro Max",
                        "storage" to "128GB SSD",
                        "color" to "Space Black",
                    ),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should return false when product is missing one required specification key`() {
        val requiredSpecs =
            mapOf(
                "color" to "red",
                "size" to "large",
                "material" to "cotton",
            )
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications =
                    mapOf(
                        "color" to "red",
                        "size" to "large",
                    ),
            )

        val result = specification.isSatisfiedBy(product)

        assertFalse(result)
    }

    @Test
    fun `should return false when product has no specifications but required specs exist`() {
        val requiredSpecs = mapOf("color" to "red")
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications = emptyMap(),
            )

        val result = specification.isSatisfiedBy(product)

        assertFalse(result)
    }

    @Test
    fun `should return false when product specification value does not contain required value`() {
        val requiredSpecs =
            mapOf(
                "color" to "blue",
                "size" to "medium",
            )
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications =
                    mapOf(
                        "color" to "red",
                        "size" to "large",
                    ),
            )

        val result = specification.isSatisfiedBy(product)

        assertFalse(result)
    }

    @Test
    fun `should return false when product has different specification key`() {
        val requiredSpecs = mapOf("COLOR" to "red")
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications = mapOf("color" to "red"),
            )

        val result = specification.isSatisfiedBy(product)

        assertFalse(result)
    }

    @Test
    fun `should return true with complex specification values containing multiple words`() {
        val requiredSpecs =
            mapOf(
                "features" to "waterproof",
                "connectivity" to "bluetooth",
            )
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications =
                    mapOf(
                        "features" to "Shockproof, Waterproof, Dustproof",
                        "connectivity" to "WiFi, Bluetooth 5.0, NFC",
                    ),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should return false when required value is not contained in specification value`() {
        val requiredSpecs = mapOf("model" to "Galaxy S22")
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications = mapOf("model" to "iPhone 15"),
            )

        val result = specification.isSatisfiedBy(product)

        assertFalse(result)
    }

    @Test
    fun `should handle empty string in required specifications`() {
        val requiredSpecs = mapOf("color" to "")
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications = mapOf("color" to "red"),
            )

        val result = specification.isSatisfiedBy(product)

        assertTrue(result)
    }

    @Test
    fun `should handle empty string in product specifications`() {
        val requiredSpecs = mapOf("color" to "red")
        val specification = SpecificationsSpecification(requiredSpecs)
        val product =
            buildProduct(
                specifications = mapOf("color" to ""),
            )

        val result = specification.isSatisfiedBy(product)

        assertFalse(result)
    }

    @Test
    fun `should handle multiple products with different specification sets`() {
        val requiredSpecs = mapOf("category" to "electronics", "condition" to "new")
        val specification = SpecificationsSpecification(requiredSpecs)

        val product1 =
            buildProduct(
                id = 1L,
                specifications = mapOf("category" to "Electronics", "condition" to "New"),
            )

        val product2 =
            buildProduct(
                id = 2L,
                specifications = mapOf("category" to "Clothing", "condition" to "New"),
            )

        val product3 =
            buildProduct(
                id = 3L,
                specifications = mapOf("category" to "Electronics", "condition" to "Used"),
            )

        assertTrue(specification.isSatisfiedBy(product1))
        assertFalse(specification.isSatisfiedBy(product2))
        assertFalse(specification.isSatisfiedBy(product3))
    }

    @Test
    fun `should work with AND specification`() {
        val specsSpec1 = SpecificationsSpecification(mapOf("color" to "red"))
        val specsSpec2 = SpecificationsSpecification(mapOf("size" to "large"))
        val combinedSpec = specsSpec1.and(specsSpec2)

        val matchingProduct =
            buildProduct(
                specifications = mapOf("color" to "red", "size" to "large"),
            )
        val nonMatchingProduct =
            buildProduct(
                specifications = mapOf("color" to "red", "size" to "medium"),
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val specsSpec1 = SpecificationsSpecification(mapOf("color" to "blue"))
        val specsSpec2 = SpecificationsSpecification(mapOf("material" to "leather"))
        val combinedSpec = specsSpec1.or(specsSpec2)

        val blueProduct =
            buildProduct(
                specifications = mapOf("color" to "blue", "material" to "cotton"),
            )
        val leatherProduct =
            buildProduct(
                specifications = mapOf("color" to "black", "material" to "leather"),
            )
        val nonMatchingProduct =
            buildProduct(
                specifications = mapOf("color" to "green", "material" to "plastic"),
            )

        assertTrue(combinedSpec.isSatisfiedBy(blueProduct))
        assertTrue(combinedSpec.isSatisfiedBy(leatherProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        // Given
        val specsSpec1 = SpecificationsSpecification(mapOf("color" to "black"))
        val specsSpec2 = SpecificationsSpecification(mapOf("storage" to "256GB"))
        val specsSpec3 = SpecificationsSpecification(mapOf("condition" to "new"))

        val combinedSpec = specsSpec1.and(specsSpec2).and(specsSpec3)

        val matchingProduct =
            buildProduct(
                specifications =
                    mapOf(
                        "color" to "black",
                        "storage" to "256GB",
                        "condition" to "new",
                    ),
            )
        val nonMatchingProduct =
            buildProduct(
                specifications =
                    mapOf(
                        "color" to "black",
                        "storage" to "256GB",
                        "condition" to "used",
                    ),
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(nonMatchingProduct))
    }
}
