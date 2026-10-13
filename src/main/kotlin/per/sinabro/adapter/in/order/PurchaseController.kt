package per.sinabro.adapter.`in`.order

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import per.sinabro.adapter.`in`.order.request.PurchaseProductRequest
import per.sinabro.application.port.`in`.order.command.PurchaseProductCommand
import per.sinabro.application.port.`in`.order.usecase.PurchaseProductUsecase

@RestController
class PurchaseController(
    private val purchaseProductUsecase: PurchaseProductUsecase
) {

    @PostMapping("/purchases")
    fun purchaseProduct(@RequestBody request: PurchaseProductRequest) {
        val (productId, quantity) = request

        purchaseProductUsecase.purchase(
            PurchaseProductCommand(
                productId = productId,
                quantity = quantity,
            )
        )
    }
}
