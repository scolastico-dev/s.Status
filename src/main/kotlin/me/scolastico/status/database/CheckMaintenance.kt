package me.scolastico.status.database

import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CheckMaintenance(

    var checkName: String,
    var fromTime: Instant,
    var untilTime: Instant?,
    var message: String

) {

    @Id
    lateinit var id: UUID

}