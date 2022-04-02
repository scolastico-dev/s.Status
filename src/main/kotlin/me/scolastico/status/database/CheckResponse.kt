package me.scolastico.status.database

import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CheckResponse(

    var at: Instant,
    var duration: Int,
    var reachable: Boolean,

) {

    @Id
    lateinit var id: UUID

}
