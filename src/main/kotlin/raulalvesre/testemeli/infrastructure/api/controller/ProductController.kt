package raulalvesre.testemeli.infrastructure.api.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import raulalvesre.testemeli.application.usecase.ProductService
import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.application.usecase.dto.ProductFilter
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.domain.enums.ProductSortField
import raulalvesre.testemeli.domain.enums.SortDirection
import raulalvesre.testemeli.infrastructure.api.helper.buildSortOrders
import raulalvesre.testemeli.infrastructure.api.mapper.toResponsePage
import raulalvesre.testemeli.infrastructure.api.response.ProductResponse

@RestController
@RequestMapping("/v1/products")
class ProductController(
    private val productService: ProductService,
) {
    @GetMapping("/{productId}")
    fun findById(
        @PathVariable productId: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.findById(productId)
        return ResponseEntity.ok(product.toResponsePage())
    }

    @GetMapping
    fun findPage(
        filter: ProductFilter,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false, name = "sortBy")
        sortBy: List<ProductSortField>?,
        @RequestParam(required = false, name = "direction")
        directions: List<SortDirection>?,
    ): ResponseEntity<Page<ProductResponse>> {
        val sortOrders = buildSortOrders(sortBy, directions)

        val query =
            ProductSearchQuery(
                filter = filter,
                page = page,
                size = size,
                sortOrders = sortOrders,
            )

        val result = productService.findPage(query)
        val responsePage = result.toResponsePage()
        return ResponseEntity.ok(responsePage)
    }
}
