package per.sinabro.domain.member

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

class Member(
    @Column(name = "username", length = 200, nullable = false, unique = true)
    val name: String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Long? = null
}
