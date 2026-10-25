package per.sinabro.adapter.out.inventory

import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import per.sinabro.domain.inventory.Inventory

interface InventoryJpaRepository : JpaRepository<Inventory, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findByProductId(productId: Long): Inventory?
}
