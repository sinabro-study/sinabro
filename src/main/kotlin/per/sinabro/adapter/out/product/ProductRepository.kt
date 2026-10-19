package per.sinabro.adapter.out.product

import org.springframework.stereotype.Repository
import per.sinabro.application.port.out.product.LoadProduct
import per.sinabro.domain.product.Product

@Repository
class ProductRepository(
    val productJpaRepository: ProductJpaRepository
) : LoadProduct {

    override fun load(product: Product): Product {
        return productJpaRepository.save(product)
    }
}
