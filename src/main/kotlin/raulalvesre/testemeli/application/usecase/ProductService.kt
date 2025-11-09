package raulalvesre.testemeli.application.usecase

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import raulalvesre.testemeli.domain.entity.Product
import raulalvesre.testemeli.domain.exception.ProductNotFoundException
import raulalvesre.testemeli.domain.pagination.PageResult
import raulalvesre.testemeli.domain.repository.ProductRepository
import raulalvesre.testemeli.domain.search.ProductSearchQuery

/**
 * Application service responsável por buscar produtos.
 *
 * Regras:
 * - findById: lança [ProductNotFoundException] se o produto não existir.
 * - findByIds: exige pelo menos um ID e limita a quantidade máxima de IDs por chamada
 *   de acordo com a propriedade `app.products.batch.max-products`.
 * - findPage: valida que o tamanho da página (size) não excede o limite máximo configurado
 *   em `app.products.batch.max-products` e aplica filtros e ordenação conforme os parâmetros
 *   da consulta.
 */
@Service
class ProductService(
    private val productRepository: ProductRepository,
    @Value("\${app.products.batch.max-products}")
    private val batchMaxProducts: Int,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun findById(id: Long): Product {
        try {
            logger.info("c=ProductService m=findById s=START productId=$id")
            val product =
                productRepository.findById(id)
                    ?: throw ProductNotFoundException(id)
            logger.info("c=ProductService m=findById s=DONE productId=$id")
            return product
        } catch (e: Exception) {
            logger.error("c=ProductService m=findById s=ERROR productId=$id message=${e.message}")
            throw e
        }
    }

    fun findByIds(ids: List<Long>): List<Product> {
        try {
            logger.info("c=ProductService m=findByIds s=START")

            if (ids.isEmpty()) {
                throw IllegalArgumentException("At least one product ID required")
            }

            if (ids.size > batchMaxProducts) {
                throw IllegalArgumentException("Cannot return more than $batchMaxProducts products. Requested: ${ids.size}")
            }

            val products = productRepository.findByIds(ids)
            logger.info("c=ProductService m=findByIds s=DONE")
            return products
        } catch (e: Exception) {
            logger.error("c=ProductService m=findByIds s=ERROR productIds=$ids message=${e.message}")
            throw e
        }
    }

    fun findPage(searchQuery: ProductSearchQuery): PageResult<Product> {
        try {
            logger.info("c=ProductService m=findPage s=START")

            if (searchQuery.size > batchMaxProducts) {
                throw IllegalArgumentException(
                    "Cannot return a page with more than $batchMaxProducts products. Requested: ${searchQuery.size}",
                )
            }

            val products = productRepository.findPage(searchQuery)
            logger.info("c=ProductService m=findPage s=DONE")
            return products
        } catch (e: Exception) {
            logger.error("c=ProductService m=findPage s=ERROR searchQuery=$searchQuery message=${e.message}")
            throw e
        }
    }
}
