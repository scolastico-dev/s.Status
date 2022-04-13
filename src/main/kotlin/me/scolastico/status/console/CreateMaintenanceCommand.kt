package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.Application
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.helper.DateHelper
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Option

@CommandLine.Command(
    name = "create-maintenance",
    description = ["Create a maintenance configuration for a check."],
    aliases = ["cm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class CreateMaintenanceCommand: Runnable {

    @CommandLine.Parameters(
        index = "0",
        description = ["The name of the check to create."]
    )
    lateinit var name: String

    @CommandLine.Parameters(
        index = "1",
        description = [
            "When to create the maintenance.",
            "Valid values are:",
            "  - now: Create the maintenance now.",
            "  - <unix timestamp>: Create the maintenance at the given unix timestamp.",
            "  - <time>: Create the maintenance at the specified time." +
                    "(The format needs to be DD/MM/YYYY HH:MM:SS and your local timezone is used.)"
        ]
    )
    lateinit var from: String

    @CommandLine.Parameters(
        index = "2",
        description = ["The message to display when the maintenance is active."]
    )
    lateinit var message: String

    @Option(
        names = ["-u", "--until"],
        description = [
            "When to end the maintenance.",
            "If not specified, the maintenance will be active until deactivated with the" +
                    "'disable-maintenance' command.",
            "Valid values are:",
            "  - now: Create the maintenance now.",
            "  - <unix timestamp>: Create the maintenance at the given unix timestamp.",
            "  - <time>: Create the maintenance at the specified time." +
                    "(The format needs to be DD/MM/YYYY HH:MM:SS and your local timezone is used.)"
        ]
    )
    var until: String? = null

    override fun run() {
        val from = DateHelper.getDate(from)
        val newUntil = if (until != null) {
            DateHelper.getDate(until!!)
        } else {
            null
        }
        if (from == null) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid from date."))
            return
        }
        if (newUntil == null && until != null) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid until date."))
            return
        }
        if (newUntil != null && newUntil < from) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Until date must be after from date."))
            return
        }
        if (!Application.config.enabledChecks.contains(name)) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check '$name' is not enabled."))
            return
        }
        val maintenance = CheckMaintenance(name, from, newUntil, message)
        DB.save(maintenance)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Created maintenance for check '$name'."))
    }

}