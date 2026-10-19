package per.sinabro.adapter.`in`.inventory

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RestController
import per.sinabro.application.port.`in`.inventory.command.SetSellableQuantityCommand
import per.sinabro.application.port.`in`.inventory.usecase.SetSellableQuantityUsecase

@RestController
class InventoryController(
    private val setSellableQuantityUsecase: SetSellableQuantityUsecase
) {

    @PutMapping("/inventories/{productId}")
    fun setSellableQuantity(@PathVariable productId: Long) {
        setSellableQuantityUsecase.setSellableQuantity(
            command = SetSellableQuantityCommand(
                productId = productId,
                quantity = 1000
            )
        )
    }
}
