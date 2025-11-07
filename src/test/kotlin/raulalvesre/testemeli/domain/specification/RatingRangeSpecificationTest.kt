package raulalvesre.testemeli.domain.specification

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import raulalvesre.testemeli.domain.specification.ProductTestFixtures.buildProduct
import java.math.BigDecimal

class RatingRangeSpecificationTest {
    @Test
    fun `should match any product when both min and max rating are null`() {
        val specification = RatingRangeSpecification(null, null)

        val productWithRating = buildProduct(rating = 4.5)
        val productWithNullRating = buildProduct(rating = null)
        val productWithZeroRating = buildProduct(rating = 0.0)
        val productWithMaxRating = buildProduct(rating = 5.0)

        assertTrue(specification.isSatisfiedBy(productWithRating))
        assertTrue(specification.isSatisfiedBy(productWithNullRating))
        assertTrue(specification.isSatisfiedBy(productWithZeroRating))
        assertTrue(specification.isSatisfiedBy(productWithMaxRating))
    }

    @Test
    fun `should match when product rating is greater than min`() {
        val specification = RatingRangeSpecification(4.0, null)

        val highRatingProduct = buildProduct(rating = 4.5)
        val exactRatingProduct = buildProduct(rating = 4.0)
        val lowRatingProduct = buildProduct(rating = 3.9)

        assertTrue(specification.isSatisfiedBy(highRatingProduct))
        assertTrue(specification.isSatisfiedBy(exactRatingProduct))
        assertFalse(specification.isSatisfiedBy(lowRatingProduct))
    }

    @Test
    fun `should handle null product rating with min rating`() {
        val specification = RatingRangeSpecification(4.0, null)
        val productWithNullRating = buildProduct(rating = null)

        assertFalse(specification.isSatisfiedBy(productWithNullRating))
    }

    @Test
    fun `should match when product rating is less than max`() {
        val specification = RatingRangeSpecification(null, 4.0)

        val lowRatingProduct = buildProduct(rating = 3.5)
        val exactRatingProduct = buildProduct(rating = 4.0)
        val highRatingProduct = buildProduct(rating = 4.1)

        assertTrue(specification.isSatisfiedBy(lowRatingProduct))
        assertTrue(specification.isSatisfiedBy(exactRatingProduct))
        assertFalse(specification.isSatisfiedBy(highRatingProduct))
    }

    @Test
    fun `should handle null product rating with max rating`() {
        val specification = RatingRangeSpecification(null, 4.0)
        val productWithNullRating = buildProduct(rating = null)

        assertTrue(specification.isSatisfiedBy(productWithNullRating))
    }

    @Test
    fun `should match when product rating is within range`() {
        val specification = RatingRangeSpecification(3.0, 4.5)

        val withinRangeProduct = buildProduct(rating = 3.5)
        val exactMinProduct = buildProduct(rating = 3.0)
        val exactMaxProduct = buildProduct(rating = 4.5)
        val belowMinProduct = buildProduct(rating = 2.9)
        val aboveMaxProduct = buildProduct(rating = 4.6)

        assertTrue(specification.isSatisfiedBy(withinRangeProduct))
        assertTrue(specification.isSatisfiedBy(exactMinProduct))
        assertTrue(specification.isSatisfiedBy(exactMaxProduct))
        assertFalse(specification.isSatisfiedBy(belowMinProduct))
        assertFalse(specification.isSatisfiedBy(aboveMaxProduct))
    }

    @Test
    fun `should handle null product rating with both min and max`() {
        val specification = RatingRangeSpecification(3.0, 4.5)
        val productWithNullRating = buildProduct(rating = null)

        assertFalse(specification.isSatisfiedBy(productWithNullRating))
    }

    @Test
    fun `should treat null rating as zero for min comparisons`() {
        val positiveMinSpec = RatingRangeSpecification(1.0, null)
        val zeroMinSpec = RatingRangeSpecification(0.0, null)
        val negativeMinSpec = RatingRangeSpecification(-1.0, null)

        val nullRatingProduct = buildProduct(rating = null)

        assertFalse(positiveMinSpec.isSatisfiedBy(nullRatingProduct))
        assertTrue(zeroMinSpec.isSatisfiedBy(nullRatingProduct))
        assertTrue(negativeMinSpec.isSatisfiedBy(nullRatingProduct))
    }

    @Test
    fun `should treat null rating as zero for max comparisons`() {
        val positiveMaxSpec = RatingRangeSpecification(null, 1.0)
        val zeroMaxSpec = RatingRangeSpecification(null, 0.0)
        val negativeMaxSpec = RatingRangeSpecification(null, -1.0)

        val nullRatingProduct = buildProduct(rating = null)

        assertTrue(positiveMaxSpec.isSatisfiedBy(nullRatingProduct))
        assertTrue(zeroMaxSpec.isSatisfiedBy(nullRatingProduct))
        assertFalse(negativeMaxSpec.isSatisfiedBy(nullRatingProduct))
    }

    @Test
    fun `should treat null rating as zero for range comparisons`() {
        val rangeIncludingZero = RatingRangeSpecification(0.0, 1.0)
        val rangeAboveZero = RatingRangeSpecification(1.0, 2.0)
        val rangeBelowZero = RatingRangeSpecification(-1.0, -0.5)

        val nullRatingProduct = buildProduct(rating = null)

        assertTrue(rangeIncludingZero.isSatisfiedBy(nullRatingProduct))
        assertFalse(rangeAboveZero.isSatisfiedBy(nullRatingProduct))
        assertFalse(rangeBelowZero.isSatisfiedBy(nullRatingProduct))
    }

    @Test
    fun `should work with AND specification`() {
        val ratingSpec = RatingRangeSpecification(4.0, null)
        val categorySpec = CategorySpecification("Electronics")
        val combinedSpec = ratingSpec.and(categorySpec)

        val matchingProduct = buildProduct(rating = 4.5, category = "Electronics")
        val lowRatedProduct = buildProduct(rating = 3.5, category = "Electronics")
        val wrongCategoryProduct = buildProduct(rating = 4.5, category = "Books")

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(lowRatedProduct))
        assertFalse(combinedSpec.isSatisfiedBy(wrongCategoryProduct))
    }

    @Test
    fun `should work with OR specification`() {
        val ratingSpec = RatingRangeSpecification(4.5, null)
        val freeShippingSpec = FreeShippingSpecification(true)
        val combinedSpec = ratingSpec.or(freeShippingSpec)

        val highlyRatedProduct = buildProduct(rating = 4.8, hasFreeShipping = false)
        val freeShippingProduct = buildProduct(rating = 3.0, hasFreeShipping = true)
        val lowRatedPaidShipping = buildProduct(rating = 3.0, hasFreeShipping = false)

        assertTrue(combinedSpec.isSatisfiedBy(highlyRatedProduct))
        assertTrue(combinedSpec.isSatisfiedBy(freeShippingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(lowRatedPaidShipping))
    }

    @Test
    fun `should work with multiple AND conditions`() {
        val ratingSpec = RatingRangeSpecification(4.0, 5.0)
        val priceSpec = PriceRangeSpecification(BigDecimal("50"), BigDecimal("500"))
        val availabilitySpec = AvailabilitySpecification(true)
        val combinedSpec = ratingSpec.and(priceSpec).and(availabilitySpec)

        val matchingProduct =
            buildProduct(
                rating = 4.5,
                price = BigDecimal("250"),
                isAvailable = true,
            )

        val lowRatedProduct =
            buildProduct(
                rating = 3.5,
                price = BigDecimal("250"),
                isAvailable = true,
            )
        val expensiveProduct =
            buildProduct(
                rating = 4.5,
                price = BigDecimal("600"),
                isAvailable = true,
            )
        val unavailableProduct =
            buildProduct(
                rating = 4.5,
                price = BigDecimal("250"),
                isAvailable = false,
            )

        assertTrue(combinedSpec.isSatisfiedBy(matchingProduct))
        assertFalse(combinedSpec.isSatisfiedBy(lowRatedProduct))
        assertFalse(combinedSpec.isSatisfiedBy(expensiveProduct))
        assertFalse(combinedSpec.isSatisfiedBy(unavailableProduct))
    }

    @Test
    fun `should handle null ratings in composite specs`() {
        val ratingSpec = RatingRangeSpecification(4.0, null)
        val brandSpec = BrandSpecification("Samsung")
        val combinedSpec = ratingSpec.and(brandSpec)

        val ratedSamsung = buildProduct(rating = 4.5, brand = "Samsung")
        val unratedSamsung = buildProduct(rating = null, brand = "Samsung")
        val ratedApple = buildProduct(rating = 4.5, brand = "Apple")

        assertTrue(combinedSpec.isSatisfiedBy(ratedSamsung))
        assertFalse(combinedSpec.isSatisfiedBy(unratedSamsung)) // null rating < 4.0
        assertFalse(combinedSpec.isSatisfiedBy(ratedApple))
    }
}
