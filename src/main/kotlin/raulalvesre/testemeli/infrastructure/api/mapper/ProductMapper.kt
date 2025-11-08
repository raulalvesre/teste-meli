package raulalvesre.testemeli.infrastructure.api.mapper

import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.infrastructure.api.response.ProductResponse

fun Product.toResponse(): ProductResponse {
    return ProductResponse(
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

fun List<Product>.toResponseList(): List<ProductResponse> {
    return this.map { it.toResponse() }
}

fun Page<Product>.toResponse(): Page<ProductResponse> {
    return Page(
        items = items.map { it.toResponse() },
        page = page,
        size = size,
        totalItems = totalItems,
    )
}
