package me.scolastico.status.console

import io.ebean.DB
import io.ktor.util.date.*
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.helper.DateHelper.Companion.getDuration
import me.scolastico.tools.etc.TableGeneratorThemes
import picocli.CommandLine.Command

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
            .addContent("ID", "Start", "End", "Duration", "Messages")
            .addTableLineSeparator()
        val downtimes = DB.createQuery(CheckDowntime::class.java).findList()
        for (downtime in downtimes) {
            var firstMessage = downtime.messages.firstOrNull()
            if (firstMessage == null) {
                firstMessage = ""
            }
            lateinit var duration: String
            val until = if (downtime.untilTime == null) {
                duration = ""
                ""
            } else {
                duration = getDuration(downtime.fromTime, downtime.untilTime!!)
                downtime.untilTime!!.toGMTDate().toString()
            }

            table.addContent(
                downtime.id.toString(),
                downtime.fromTime.toGMTDate().toString(),
                until,
                duration,
                firstMessage
            )

            if (downtime.messages.size > 1) {
                downtime.messages.drop(1).forEach {
                    table.addContent("", "", "", "", it)
                }
            }
        }
        println(table)
    }

}