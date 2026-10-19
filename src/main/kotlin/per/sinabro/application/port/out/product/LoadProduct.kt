package per.sinabro.application.port.out.product

import per.sinabro.domain.product.Product

interface LoadProduct {
    fun load(product: Product): Product
}
