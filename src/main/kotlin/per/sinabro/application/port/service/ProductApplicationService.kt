package per.sinabro.application.port.service

import org.springframework.stereotype.Service
import per.sinabro.application.port.`in`.product.command.RegisterProductCommand
import per.sinabro.application.port.`in`.product.usecase.RegisterProductUsecase
import per.sinabro.application.port.out.product.LoadProduct
import per.sinabro.domain.product.Product

@Service
class ProductApplicationService(
    private val loadProduct: LoadProduct
) : RegisterProductUsecase {

    override fun register(command: RegisterProductCommand) {
        val (name, price) = command

        loadProduct.load(product = Product(
            name = name,
            price = price
        ))
    }
}
