package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckMaintenance
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Command

@Command(
    name = "delete-maintenance",
    description = ["Delete a maintenance"],
    aliases = ["dm"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class DeleteMaintenanceCommand: Runnable {

    @CommandLine.Parameters(
        index = "0",
        description = [
            "The id of the maintenance.",
            "Only the first unique characters are needed."
        ]
    )
    lateinit var id : String

    override fun run() {
        val query = DB.createQuery(CheckMaintenance::class.java).where().startsWith("id", id)
        val count = query.findCount()
        if (count > 1) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("More than one maintenance with the same id."))
            return
        }
        if (query.delete() == 0) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("No maintenance with the given id."))
            return
        }
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Maintenance deleted."))
    }

}