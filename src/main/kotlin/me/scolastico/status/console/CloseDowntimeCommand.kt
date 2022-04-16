package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.helper.DateHelper
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Command
import java.time.Instant

@Command(
    name = "close-downtime",
    description = ["Close a downtime."],
    aliases = ["cd"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class CloseDowntimeCommand: Runnable {

    @CommandLine.Option(
        names = ["-a", "--at"],
        description = [
            "When to create the maintenance.",
            "Valid values are:",
            "  - now: Create the maintenance now.",
            "  - <unix timestamp>: Create the maintenance at the given unix timestamp.",
            "  - <time>: Create the maintenance at the specified time."
        ]
    )
    var at: String? = null

    @CommandLine.Parameters(
        index = "0",
        description = [
            "The id of the downtime entry to delete.",
            "Only the first unique characters are needed."
        ]
    )
    lateinit var id : String

    override fun run() {
        val query = DB.createQuery(CheckDowntime::class.java).where().startsWith("id", id)
        val count = query.findCount()
        if (count > 1) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("More than one downtime entry found."))
            return
        }
        if (query.delete() == 0) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("No downtime entry found."))
            return
        }
        val downtime = query.findOne()!!
        if (downtime.untilTime != null) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Downtime entry is already closed."))
            return
        }
        var time = Instant.now()
        if (at != null) {
            val newTime = DateHelper.getDate(at!!)
            if (newTime == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid time format."))
                return
            }
            if (newTime.isAfter(time)) {
                println(Ansi.ansi().fgYellow().a("Warning: ").fgDefault().a("Time is in the future."))
            }
            time = newTime
        }
        downtime.untilTime = time
        DB.save(downtime)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Downtime entry closed."))
    }

}