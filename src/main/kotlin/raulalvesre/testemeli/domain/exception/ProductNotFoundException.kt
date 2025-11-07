package raulalvesre.testemeli.domain.exception

class ProductNotFoundException(
    productId: Long,
) : RuntimeException("Product with id=$productId not found")
