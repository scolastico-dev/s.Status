package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.helper.DateHelper
import me.scolastico.status.helper.DateHelper.Companion.getDuration
import me.scolastico.tools.etc.TableGeneratorThemes
import picocli.CommandLine.Command
import java.time.Instant

@Command(
    name = "list-downtimes",
    description = ["List all downtimes"],
    aliases = ["ld"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ListDowntimesCommand: Runnable {

    override fun run() {
        val table = TableGeneratorThemes.FANCY_BOARDER()
            .addContent("ID", "Start (UTC)", "End (UTC)", "Duration", "Status", "Messages")
            .addTableLineSeparator()
        val downtimes = DB.createQuery(CheckDowntime::class.java).findList()
        for (downtime in downtimes) {
            lateinit var duration: String
            val until = if (downtime.untilTime == null) {
                duration = getDuration(downtime.fromTime, Instant.now())
                ""
            } else {
                duration = getDuration(downtime.fromTime, downtime.untilTime!!)
                DateHelper.getUTCTime(downtime.untilTime!!)
            }
            val status = if (downtime.yellow) {
                "yellow"
            } else {
                "red"
            }
            table.addContent(
                downtime.id.toString(),
                DateHelper.getUTCTime(downtime.fromTime),
                until,
                duration,
                status,
                downtime.messages.first()
            )

            if (downtime.messages.size > 1) {
                downtime.messages.drop(1).forEach {
                    table.addContent("", "", "", "", "", it)
                }
            }
        }
        println(table)
    }

}