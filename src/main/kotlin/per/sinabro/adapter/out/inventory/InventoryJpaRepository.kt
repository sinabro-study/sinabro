package per.sinabro.adapter.out.inventory

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.inventory.Inventory

interface InventoryJpaRepository : JpaRepository<Inventory, Long> {
    fun findByProductId(productId: Long): Inventory?
}
