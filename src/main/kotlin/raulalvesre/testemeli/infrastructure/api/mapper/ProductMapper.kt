package raulalvesre.testemeli.infrastructure.api.mapper

import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.pagination.PageResult
import raulalvesre.testemeli.infrastructure.api.response.ProductDetailResponse
import raulalvesre.testemeli.infrastructure.api.response.ProductSummaryResponse

fun Product.toDetailResponse(): ProductDetailResponse {
    return ProductDetailResponse(
        id = id,
        title = name,
        description = description,
        price = price,
        brand = brand,
        imageUrl = imageUrl,
        category = category,
        specifications = specifications,
        condition = condition,
        rating = rating,
        totalReviews = totalReviews,
        hasFreeShipping = hasFreeShipping,
        isAvailable = isAvailable,
    )
}

fun Product.toSummaryResponse(): ProductSummaryResponse {
    return ProductSummaryResponse(
        id = id,
        title = name,
        price = price,
        imageUrl = imageUrl,
        condition = condition,
        rating = rating,
        totalReviews = totalReviews,
        hasFreeShipping = hasFreeShipping,
        isAvailable = isAvailable,
    )
}

fun List<Product>.toDetailResponseList(): List<ProductDetailResponse> {
    return this.map { it.toDetailResponse() }
}

fun PageResult<Product>.toSummaryResponsePage(): PageResult<ProductSummaryResponse> {
    return PageResult(
        items = items.map { it.toSummaryResponse() },
        page = page,
        size = size,
        totalItems = totalItems,
    )
}
