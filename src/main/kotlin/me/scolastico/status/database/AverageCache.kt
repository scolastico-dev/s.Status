package me.scolastico.status.database

import java.time.Instant
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class AverageCache(

    var checkName: String,

    @Column(length = 3)
    var uptime: Int,

    var duration: Int,

    var at: Instant,

    @Column(length = 2)
    var timezone: Int,

) {

    @Id
    lateinit var id: UUID

}
