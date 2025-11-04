package per.sinabro.application.port.`in`.ticket.usecase

interface ReserveTicket {
    fun reserve(userId: Long, ticketId: Long): Long
    fun reservePriority(userId: Long, ticketId: Long): Long
    fun reservePriorityV2(userId: Long, ticketId: Long): Long
}
