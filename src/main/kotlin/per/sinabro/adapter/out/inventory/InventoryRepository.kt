package per.sinabro.adapter.out.inventory

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.inventory.ChangeInventory
import per.sinabro.application.port.out.inventory.FindInventory
import per.sinabro.application.port.out.inventory.LoadInventory
import per.sinabro.domain.inventory.Inventory

@Repository
class InventoryRepository(
    private val inventoryJpaRepository: InventoryJpaRepository
) : LoadInventory, FindInventory, ChangeInventory {

    override fun load(inventory: Inventory): Inventory {
        return inventoryJpaRepository.save(inventory)
    }

    override fun findByProductIdForUpdate(productId: Long): Inventory? {
        return inventoryJpaRepository.findWithLockingByProductId(productId)
    }

    override fun change(productId: Long, quantity: Int): Int {
        return inventoryJpaRepository.updateInventory(productId, quantity)
    }
}
