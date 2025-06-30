package per.sinabro.port.out

import per.sinabro.domain.Ticket

interface FindTicket {
    fun findByIdIsReserved(id: Long, isReservation: Boolean): Ticket?
}
