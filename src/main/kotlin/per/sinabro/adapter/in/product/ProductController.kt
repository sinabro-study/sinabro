package per.sinabro.adapter.`in`.product

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import per.sinabro.application.port.`in`.product.command.RegisterProductCommand
import per.sinabro.application.port.`in`.product.usecase.RegisterProductUsecase
import java.math.BigDecimal

@RestController
class ProductController(
    private val registerProductUsecase: RegisterProductUsecase
) {

    @PostMapping("/product")
    fun register() {
        registerProductUsecase.register(
            RegisterProductCommand("치이카와", BigDecimal(1000.0))
        )
    }
}