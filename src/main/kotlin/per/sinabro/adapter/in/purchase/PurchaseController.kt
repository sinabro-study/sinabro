package per.sinabro.adapter.`in`.purchase

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import per.sinabro.adapter.`in`.purchase.request.PurchaseProductRequest
import per.sinabro.application.port.`in`.order.command.PurchaseProductCommand
import per.sinabro.application.port.`in`.order.usecase.PurchaseProductUsecase

@RestController
class PurchaseController(
    private val purchaseProductUsecase: PurchaseProductUsecase
) {

    @PostMapping("/purchases")
    fun purchaseProduct(@RequestBody request: PurchaseProductRequest) {
        val (memberId, productId, quantity) = request

        purchaseProductUsecase.purchase(
            PurchaseProductCommand(
                memberId = memberId,
                productId = productId,
                quantity = quantity,
            )
        )
    }
}
