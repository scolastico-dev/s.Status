package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.helper.DateHelper
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Command
import picocli.CommandLine.Option
import java.time.Instant

@Command(
    name = "convert-downtime",
    description = ["Convert a downtime to an maintenance status."],
    aliases = ["cdt"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ConvertDowntimeCommand: Runnable {

    @Option(
        names = ["--force"],
        description = ["Force the downtime to be converted."]
    )
    var force = false

    @Option(
        names = ["-u", "--until"],
        description = ["Set the until date for the new maintenance status."]
    )
    var until: String? = null

    @Option(
        names = ["-f", "--from"],
        description = ["Set the from date for the new maintenance status."]
    )
    var from: String? = null

    @CommandLine.Parameters(
        index = "0",
        description = [
            "The id of the downtime entry to delete.",
            "Only the first unique characters are needed."
        ]
    )
    lateinit var id : String

    @CommandLine.Parameters(
        index = "1",
        description = ["The message of the maintenance."]
    )
    lateinit var message : String

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
        if (downtime.untilTime != null && !force) {
            println(Ansi.ansi().fgRed()
                .a("Error: ").fgDefault().a("Downtime is already resolved.")
                .fgYellow().a(" Use --force to force the conversion.")
            )
            return
        }
        var until: Instant? = downtime.untilTime
        if (this.until != null) {
            until = DateHelper.getDate(this.until!!)
            if (until == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid date."))
                return
            }
        }
        var from: Instant? = downtime.fromTime
        if (this.from != null) {
            from = DateHelper.getDate(this.from!!)
            if (from == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid date."))
                return
            }
        }
        val maintenance = CheckMaintenance(
            checkName = downtime.checkName,
            fromTime = from!!,
            untilTime = until,
            message = message
        )
        DB.delete(downtime)
        DB.save(maintenance)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Downtime converted to maintenance."))
    }

}