package me.scolastico.status.database

import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class StartedActions(

    val checkName: String,
    val action: String

) {

    @Id
    lateinit var id: UUID

    var at: Instant = Instant.now()

}
