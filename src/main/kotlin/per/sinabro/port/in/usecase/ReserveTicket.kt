package per.sinabro.port.`in`.usecase

interface ReserveTicket {
    fun reserve(userId: Long, ticketId: Long): Long
    fun reservePriority(userId: Long, ticketId: Long): Long
}
