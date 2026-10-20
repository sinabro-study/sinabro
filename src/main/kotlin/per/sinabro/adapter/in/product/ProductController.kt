package per.sinabro.adapter.`in`.product

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import per.sinabro.application.port.`in`.product.command.RegisterProductCommand
import per.sinabro.application.port.`in`.product.usecase.RegisterProductUsecase

@RestController
class ProductController(
    private val registerProductUsecase: RegisterProductUsecase
) {

    @PostMapping("/product")
    fun register(@RequestBody command: RegisterProductCommand): Long {
        return registerProductUsecase.register(command)
    }
}
