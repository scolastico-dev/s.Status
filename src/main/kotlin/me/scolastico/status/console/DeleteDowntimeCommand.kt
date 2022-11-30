package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.database.CheckDowntime
import org.fusesource.jansi.Ansi
import picocli.CommandLine

@CommandLine.Command(
    name = "delete-downtime",
    description = ["Delete a downtime entry."],
    aliases = ["dd"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class DeleteDowntimeCommand: Runnable {

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
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Downtime entry deleted."))
    }

}