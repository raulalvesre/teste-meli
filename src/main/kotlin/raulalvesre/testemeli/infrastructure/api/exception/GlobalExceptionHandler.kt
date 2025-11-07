package raulalvesre.testemeli.infrastructure.api.exception

import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import raulalvesre.testemeli.domain.exception.ProductNotFoundException

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException::class)
    fun handleProductNotFound(
        ex: ProductNotFoundException,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val status = HttpStatus.NOT_FOUND
        val body =
            ApiErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                message = ex.message,
                path = request.requestURI,
            )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(
        MethodArgumentNotValidException::class,
        MethodArgumentTypeMismatchException::class,
        MissingServletRequestParameterException::class,
        HttpMessageNotReadableException::class,
        IllegalArgumentException::class,
    )
    fun handleBadRequest(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val status = HttpStatus.BAD_REQUEST

        val message =
            when (ex) {
                is MethodArgumentNotValidException ->
                    ex.bindingResult.fieldErrors.joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
                is MethodArgumentTypeMismatchException ->
                    "Invalid value for parameter '${ex.name}'"
                is MissingServletRequestParameterException ->
                    "Missing required parameter '${ex.parameterName}'"
                is HttpMessageNotReadableException ->
                    "Malformed JSON request"
                else ->
                    ex.message
            }

        val body =
            ApiErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                message = message,
                path = request.requestURI,
            )
        return ResponseEntity.status(status).body(body)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericError(
        ex: Exception,
        request: HttpServletRequest,
    ): ResponseEntity<ApiErrorResponse> {
        val status = HttpStatus.INTERNAL_SERVER_ERROR
        val body =
            ApiErrorResponse(
                status = status.value(),
                error = status.reasonPhrase,
                message = "Unexpected error occurred",
                path = request.requestURI,
            )

        return ResponseEntity.status(status).body(body)
    }
}
