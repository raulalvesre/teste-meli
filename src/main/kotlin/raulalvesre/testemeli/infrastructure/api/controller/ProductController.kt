package raulalvesre.testemeli.infrastructure.api.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Schema
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
import raulalvesre.testemeli.infrastructure.api.mapper.toDetailResponse
import raulalvesre.testemeli.infrastructure.api.mapper.toDetailResponseList
import raulalvesre.testemeli.infrastructure.api.mapper.toSummaryResponsePage
import raulalvesre.testemeli.infrastructure.api.request.ProductFilterRequest
import raulalvesre.testemeli.infrastructure.api.response.ProductDetailResponse
import raulalvesre.testemeli.infrastructure.api.response.ProductSummaryResponse

@RestController
@RequestMapping("/v1/products")
class ProductController(
    private val productService: ProductService,
) {
    @Operation(summary = "Buscar produto por ID")
    @GetMapping("/{id}")
    fun findById(
        @PathVariable id: Long,
    ): ResponseEntity<ProductDetailResponse> {
        val product = productService.findById(id)
        return ResponseEntity.ok(product.toDetailResponse())
    }

    @Operation(
        summary = "Buscar produtos por IDs",
        description = "Permite buscar produtos em lote informando uma lista de IDs (máx. 50 por requisição).",
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Operação bem-sucedida"),
            ApiResponse(responseCode = "400", description = "Requisição inválida (lista de IDs ultrapassa o limite de 50)."),
        ],
    )
    @GetMapping("/batch")
    fun findByIds(
        @RequestParam(name = "ids") ids: List<Long>,
    ): ResponseEntity<List<ProductDetailResponse>> {
        val result = productService.findByIds(ids)
        val responsePage = result.toDetailResponseList()
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
        @RequestParam page: Int = 0,
        @RequestParam size: Int = 10,
        @Parameter(
            description = "Campos para ordenação dos resultados. Pode ser múltiplos campos na forma: sortBy=name&sortBy=price",
            array =
                ArraySchema(
                    schema =
                        Schema(
                            type = "string",
                            allowableValues = ["name", "price", "rating", "brand"],
                            example = "name",
                        ),
                ),
        )
        @RequestParam(required = false, name = "sortBy")
        sortBy: List<String> = emptyList(),
        @Parameter(
            description = "Direção da ordenação para cada campo. Deve corresponder à ordem dos campos em 'sortBy'. Ex: sortBy=name&direction=asc",
            array =
                ArraySchema(
                    schema =
                        Schema(
                            type = "string",
                            allowableValues = ["asc", "desc"],
                            example = "desc",
                        ),
                ),
        )
        @RequestParam(required = false, name = "direction")
        directions: List<String> = emptyList(),
    ): ResponseEntity<Page<ProductSummaryResponse>> {
        if (page < 0) {
            throw IllegalArgumentException("Invalid page param. Page should not be negative.")
        }

        if (size <= 0) {
            throw IllegalArgumentException("Invalid size param. Size should be greater than zero.")
        }

        // Relaciona os campos solicitados com suas respectivas direções, assumindo ASC quando o
        // cliente omite a direção. Assim, o caso de uso recebe uma lista normalizada de SortOrder
        // mesmo em requisições parcialmente preenchidas.
        val sortOrders = buildSortOrders(sortBy, directions)

        val query =
            ProductSearchQuery(
                filter = filter.toApplicationFilter(),
                page = page,
                size = size,
                sortOrders = sortOrders,
            )

        val result = productService.findPage(query)
        val responsePage = result.toSummaryResponsePage()
        return ResponseEntity.ok(responsePage)
    }
}
