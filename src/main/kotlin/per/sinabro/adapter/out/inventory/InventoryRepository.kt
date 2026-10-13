package per.sinabro.adapter.out.inventory

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.inventory.FindInventory
import per.sinabro.application.port.out.inventory.LoadInventory
import per.sinabro.domain.inventory.Inventory

@Repository
class InventoryRepository(
    private val inventoryJpaRepository: InventoryJpaRepository
) : LoadInventory, FindInventory {

    override fun load(inventory: Inventory): Inventory {
        return inventoryJpaRepository.save(inventory)
    }

    override fun findByProductId(productId: Long): Inventory? {
        return inventoryJpaRepository.findByProductId(productId)
    }
}