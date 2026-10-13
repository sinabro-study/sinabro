package per.sinabro.application.port.service

import org.springframework.stereotype.Service
import per.sinabro.application.port.`in`.inventory.command.SetSellableQuantityCommand
import per.sinabro.application.port.`in`.inventory.usecase.SetSellableQuantityUsecase
import per.sinabro.application.port.out.inventory.FindInventory
import per.sinabro.application.port.out.inventory.LoadInventory
import per.sinabro.domain.inventory.Inventory

@Service
class InventoryApplicationService(
    private val loadInventory: LoadInventory,
    private val findInventory: FindInventory,
) : SetSellableQuantityUsecase {

    override fun setSellableQuantity(command: SetSellableQuantityCommand) {
        val (productId, quantity) = command
        val inventory = findInventory.findByProductId(productId)

        if (inventory == null) {
            loadInventory.load(inventory = Inventory(productId, quantity))
            return
        }

        inventory.changeQuantity(quantity)
    }
}
