package per.sinabro.domain.inventory

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Version

@Entity
class Inventory(
    @Column(name = "product_id", unique = true, nullable = false)
    val productId: Long,
    quantity: Int,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    val id: Long? = null

    @Column(name = "quantity", nullable = false)
    var quantity: Int = quantity
        protected set

    @Version
    @Column(name = "version", nullable = false)
    val version: Long = 0

    fun changeQuantity(quantity: Int) {
        require(quantity > 0) { "수량은 양수여야합니다." }
        this.quantity = quantity
    }

    fun subtract(quantity: Int) {
        require(quantity > 0) { "차감 수량은 양수여야합니다." }
        require(this.quantity > 0) { "잔여 수량은 양수여야합니다." }
        this.quantity -= quantity
    }
}
