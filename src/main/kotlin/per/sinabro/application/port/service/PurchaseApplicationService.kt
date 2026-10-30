package per.sinabro.application.port.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import per.sinabro.application.port.`in`.order.command.PurchaseProductCommand
import per.sinabro.application.port.`in`.order.usecase.PurchaseProductUsecase
import per.sinabro.application.port.out.inventory.ChangeInventory
import per.sinabro.application.port.out.inventory.FindInventory
import per.sinabro.application.port.out.inventory.LoadInventory
import per.sinabro.application.port.out.order.LoadOrder
import per.sinabro.domain.inventory.OutOfStockException
import per.sinabro.domain.order.Order

@Service
class PurchaseApplicationService(
    private val findInventory: FindInventory,
    private val changeInventory: ChangeInventory,
    private val loadOrder: LoadOrder
) : PurchaseProductUsecase {

    @Transactional
    override fun purchase(command: PurchaseProductCommand) {
        val (memberId, productId, quantity) = command

        deductInventory(productId, quantity)

        val order = Order(memberId = memberId, productId = productId, quantity = quantity)
        loadOrder.load(order)
    }

    private fun deductInventory(productId: Long, quantity: Int) {
        if (true) {
            val inventory = findInventory.findByProductIdForUpdate(productId)
                ?: throw IllegalArgumentException("Product not found: $productId")

            inventory.subtract(quantity)
        } else {
            val success = changeInventory.change(productId, quantity)

            if (success == 0) {
                throw OutOfStockException(0, quantity)
            }
        }
    }
}
