package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.helper.DateHelper
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Command

@Command(
    name = "update-maintenance",
    aliases = ["um"],
    description = ["Update an maintenance information."],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class UpdateMaintenanceCommand: Runnable {

    @CommandLine.Option(
        names = ["-f", "--from"],
        description = ["The start date of the maintenance."]
    )
    var from: String? = null

    @CommandLine.Option(
        names = ["-u", "--until"],
        description = ["The date until the maintenance ends."]
    )
    var until: String? = null

    @CommandLine.Option(
        names = ["--delete-until"],
        description = ["Delete the maintenance until date."]
    )
    var delUntil: Boolean = false

    @CommandLine.Option(
        names = ["-m", "--message"],
        description = ["The message of the maintenance."]
    )
    var message: String? = null

    @CommandLine.Parameters(
        index = "0",
        description = [
            "The id of the maintenance.",
            "Only the first unique characters are needed."
        ]
    )
    lateinit var id: String

    override fun run() {
        if (from == null && until == null && message == null) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("You must specify at least one option."))
            return
        }
        if (until != null && delUntil) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("You cannot specify both 'until' and 'delete-until'."))
            return
        }
        val query = DB.createQuery(CheckMaintenance::class.java).where().startsWith("id", id)
        val count = query.findCount()
        if (count > 1) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("More than one maintenance with the same id."))
            return
        }
        if (count == 0) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("No maintenance with the given id."))
            return
        }
        val maintenance = query.findOne()!!
        if (from != null) {
            val from = DateHelper.getDate(from!!)
            if (from == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid from date format."))
                return
            }
            maintenance.fromTime = from
        }
        if (until != null) {
            val until = DateHelper.getDate(until!!)
            if (until == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid until date format."))
                return
            }
            maintenance.untilTime = until
        }
        if (message != null) {
            maintenance.message = message!!
        }
        if (delUntil) {
            maintenance.untilTime = null
        }
        DB.save(maintenance)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Maintenance updated."))
    }

}