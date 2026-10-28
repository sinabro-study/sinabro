package per.sinabro.domain.inventory

import jakarta.persistence.*

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

    fun changeQuantity(quantity: Int) {
        require(quantity > 0) { "수량은 양수여야합니다." }
        this.quantity = quantity
    }

    fun subtract(quantity: Int) {
        require(quantity > 0) { "차감 수량은 양수여야합니다." }
        if (this.quantity < quantity) throw OutOfStockException(this.quantity, quantity)
        this.quantity -= quantity
    }
}
