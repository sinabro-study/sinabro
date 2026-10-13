package per.sinabro.application.port.`in`.order.usecase

import per.sinabro.application.port.`in`.order.command.PurchaseProductCommand

interface PurchaseProductUsecase {
    fun purchase(command: PurchaseProductCommand)
}
