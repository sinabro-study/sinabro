package per.sinabro.application.port.`in`.product.usecase

import per.sinabro.application.port.`in`.product.command.RegisterProductCommand

interface RegisterProductUsecase {
    fun register(command: RegisterProductCommand)
}