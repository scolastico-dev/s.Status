package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.helper.DateHelper
import me.scolastico.tools.etc.TableGeneratorThemes
import picocli.CommandLine.Command
import java.time.Instant

@Command(
    name = "list-maintenances",
    description = ["List all maintenances"],
    aliases = ["lm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ListMaintenancesCommand: Runnable {

    override fun run() {
        val table = TableGeneratorThemes.FANCY_BOARDER().addContent("ID", "Start (UTC)", "End (UTC)", "Duration", "Message")
        val maintenances = DB.createQuery(CheckMaintenance::class.java).findList()
        for (maintenance in maintenances) {
            lateinit var duration: String
            val until = if (maintenance.untilTime == null) {
                duration = DateHelper.getDuration(maintenance.fromTime, Instant.now())
                ""
            } else {
                duration = DateHelper.getDuration(maintenance.fromTime, maintenance.untilTime!!)
                DateHelper.getUTCTime(maintenance.untilTime!!)
            }
            table.addContent(
                maintenance.id.toString(),
                DateHelper.getUTCTime(maintenance.fromTime),
                until,
                duration,
                maintenance.message
            )
        }
        println(table)
    }

}