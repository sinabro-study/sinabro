package per.sinabro.domain.product

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.math.BigDecimal

@Entity
class Product(
    @Column(name = "product_name", length = 200, nullable = false)
    val name: String,

    @Column(precision = 12, scale = 2)
    val price: BigDecimal
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Long? = null
}
