package me.scolastico.status.database

import io.ebean.annotation.DbArray
import me.scolastico.status.Application
import java.time.Instant
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CheckDowntime(

    var checkName: String,
    var fromTime: Instant,
    var untilTime: Instant? = null,
    var yellow: Boolean = false,

    @DbArray
    var messages: MutableList<String> = mutableListOf(Application.config.defaultDowntimeMessage)

) {

    @Id
    lateinit var id: UUID

}
