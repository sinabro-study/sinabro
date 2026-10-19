package per.sinabro.application.port.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import per.sinabro.application.port.`in`.order.command.PurchaseProductCommand
import per.sinabro.application.port.`in`.order.usecase.PurchaseProductUsecase
import per.sinabro.application.port.out.inventory.FindInventory

@Service
class PurchaseApplicationService(
    private val findInventory: FindInventory
) : PurchaseProductUsecase {

    @Transactional
    override fun purchase(command: PurchaseProductCommand) {
        val (productId, quantity) = command

        val inventory = findInventory.findByProductId(productId)
            ?: throw IllegalArgumentException("Product not found: $productId")

        inventory.subtract(quantity)
    }
}
