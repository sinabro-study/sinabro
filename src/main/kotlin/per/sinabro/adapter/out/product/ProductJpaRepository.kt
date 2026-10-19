package per.sinabro.adapter.out.product

import org.springframework.data.jpa.repository.JpaRepository
import per.sinabro.domain.product.Product

interface ProductJpaRepository : JpaRepository<Product, Long>
