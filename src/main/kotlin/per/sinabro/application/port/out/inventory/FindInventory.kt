package per.sinabro.application.port.out.inventory

import per.sinabro.domain.inventory.Inventory

interface FindInventory {
    fun findByProductId(productId: Long): Inventory?
}
