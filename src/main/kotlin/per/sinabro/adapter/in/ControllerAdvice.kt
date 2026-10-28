package per.sinabro.adapter.`in`

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import per.sinabro.domain.inventory.OutOfStockException

@RestControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(OutOfStockException::class)
    fun outOfStock(e: OutOfStockException): ResponseEntity<Map<String, String>> {
        return ResponseEntity
            .status(HttpStatus.UNPROCESSABLE_ENTITY)
            .body(mapOf("code" to "OUT_OF_STOCK"))
    }
}
