package raulalvesre.testemeli.infrastructure.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springdoc.core.annotations.ParameterObject
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import raulalvesre.testemeli.application.usecase.ProductService
import raulalvesre.testemeli.application.usecase.dto.Page
import raulalvesre.testemeli.application.usecase.dto.ProductSearchQuery
import raulalvesre.testemeli.infrastructure.api.helper.buildSortOrders
import raulalvesre.testemeli.infrastructure.api.mapper.ProductFilterMapper.toApplicationFilter
import raulalvesre.testemeli.infrastructure.api.mapper.toResponse
import raulalvesre.testemeli.infrastructure.api.mapper.toResponseList
import raulalvesre.testemeli.infrastructure.api.request.ProductFilterRequest
import raulalvesre.testemeli.infrastructure.api.response.ProductResponse

@RestController
@RequestMapping("/v1/products")
class ProductController(
    private val productService: ProductService,
) {

    @Operation(summary = "Buscar produto por ID")
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Long,
    ): ResponseEntity<ProductResponse> {
        val product = productService.findById(id)
        return ResponseEntity.ok(product.toResponse())
    }

    @Operation(
        summary = "Buscar produtos por IDs",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
        ],
    )
    @GetMapping("/batch")
    fun findByIds(ids: List<Long>): ResponseEntity<List<ProductResponse>> {
        val result = productService.findByIds(ids)
        val responsePage = result.toResponseList()
        return ResponseEntity.ok(responsePage)
    }

    @Operation(
        summary = "Buscar produtos paginados",
        description = "Retorna uma lista paginada de produtos com filtros",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            ApiResponse(responseCode = "400", description = "Parâmetros inválidos"),
        ],
    )
    @GetMapping
    fun findPage(
        @ParameterObject filter: ProductFilterRequest,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(required = false, name = "sortBy")
        sortBy: List<String> = emptyList(),
        @RequestParam(required = false, name = "direction")
        directions: List<String> = emptyList(),
    ): ResponseEntity<Page<ProductResponse>> {
        val sortOrders = buildSortOrders(sortBy, directions)

        val query =
            ProductSearchQuery(
                filter = filter.toApplicationFilter(),
                page = page,
                size = size,
                sortOrders = sortOrders,
            )

        val result = productService.findPage(query)
        val responsePage = result.toResponse()
        return ResponseEntity.ok(responsePage)
    }
}
