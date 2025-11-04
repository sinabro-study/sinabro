package per.sinabro.application.port.out.ticket

import per.sinabro.domain.ticket.Ticket

interface FindTicket {
    fun findByIdIsReserved(id: Long, isReservation: Boolean): Ticket?
    fun findByIdIsReservedForPriority(id: Long, isReservation: Boolean): Ticket?
}
