package me.scolastico.status.database

import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CheckResponse(

    var checkName: String,
    var duration: Int,

) {

    @Id
    lateinit var id: UUID

    var at: Instant = Instant.now()

}
