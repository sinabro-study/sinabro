package per.sinabro.port.`in`.ticket.usecase

interface ReserveTicket {
    fun reserve(userId: Long, ticketId: Long): Long
    fun reservePriority(userId: Long, ticketId: Long): Long
    fun reservePriorityV2(userId: Long, ticketId: Long): Long
}
