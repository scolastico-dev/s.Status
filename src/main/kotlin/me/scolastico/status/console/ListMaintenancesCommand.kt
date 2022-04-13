package me.scolastico.status.console

import io.ebean.DB
import io.ktor.util.date.*
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.helper.DateHelper
import me.scolastico.tools.etc.TableGeneratorThemes
import picocli.CommandLine.Command

@Command(
    name = "list-maintenances",
    description = ["List all maintenances"],
    aliases = ["lm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ListMaintenancesCommand: Runnable {

    override fun run() {
        val table = TableGeneratorThemes.FANCY_BOARDER().addContent("ID")
        val maintenances = DB.createQuery(CheckMaintenance::class.java).findList()
        for (maintenance in maintenances) {
            lateinit var duration: String
            val until = if (maintenance.untilTime == null) {
                duration = ""
                ""
            } else {
                duration = DateHelper.getDuration(maintenance.fromTime, maintenance.untilTime!!)
                maintenance.untilTime!!.toGMTDate().toString()
            }
            table.addContent(
                maintenance.id.toString(),
                maintenance.fromTime.toGMTDate().toString(),
                until,
                duration,
                maintenance.message
            )
        }
        println(table)
    }

}