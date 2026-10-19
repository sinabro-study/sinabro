package per.sinabro.application.port.`in`.product.command

import java.math.BigDecimal

data class RegisterProductCommand(
    val name: String,
    val price: BigDecimal
)
