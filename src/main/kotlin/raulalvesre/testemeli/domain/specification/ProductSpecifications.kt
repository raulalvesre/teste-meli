package raulalvesre.testemeli.domain.specification

import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.enums.ProductCondition
import java.math.BigDecimal

class NameSpecification(private val name: String?) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean = name == null || item.name.contains(name.trim(), ignoreCase = true)
}

class BrandSpecification(private val brand: String?) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean = brand == null || item.brand?.contains(brand.trim(), ignoreCase = true) == true
}

class CategorySpecification(private val category: String?) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean = category == null || item.category.contains(category.trim(), ignoreCase = true)
}

class SpecificationSpecifications(private val requiredSpecs: Map<String, String>) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean {
        return requiredSpecs.all { (key, expectedValue) ->
            item.specifications[key]?.contains(expectedValue, ignoreCase = true) ?: false
        }
    }
}

class ConditionSpecification(private val condition: ProductCondition?) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean = condition == null || item.condition == condition
}

class PriceRangeSpecification(
    private val minPrice: BigDecimal?,
    private val maxPrice: BigDecimal?,
) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean =
        (minPrice == null || item.price >= minPrice) &&
            (maxPrice == null || item.price <= maxPrice)
}

class RatingRangeSpecification(
    private val minRating: Double?,
    private val maxRating: Double?,
) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean =
        (minRating == null || (item.rating ?: 0.0) >= minRating) &&
            (maxRating == null || (item.rating ?: 0.0) <= maxRating)
}

class FreeShippingSpecification(private val freeShipping: Boolean?) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean = freeShipping == null || item.hasFreeShipping == freeShipping
}

class AvailabilitySpecification(private val isAvailable: Boolean?) : Specification<Product> {
    override fun isSatisfiedBy(item: Product): Boolean = isAvailable == null || item.isAvailable == isAvailable
}
