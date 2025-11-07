package raulalvesre.testemeli.infrastructure.api.mapper

import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.infrastructure.api.response.ProductResponse

fun Product.toResponsePage(): ProductResponse {
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

fun Page<Product>.toResponsePage(): Page<ProductResponse> {
    return Page(
        items = items.map { it.toResponsePage() },
        page = page,
        size = size,
        totalItems = totalItems,
    )
}
