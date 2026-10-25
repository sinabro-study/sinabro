package per.sinabro.application.port.out.inventory

import per.sinabro.domain.inventory.Inventory

interface FindInventory {
    fun findByProductIdForUpdate(productId: Long): Inventory?
}
