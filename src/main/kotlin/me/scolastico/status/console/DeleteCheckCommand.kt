package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.Application
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.database.CheckResponse
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import java.io.File

@CommandLine.Command(
    name = "delete-check",
    description = ["Delete a check configuration."],
    aliases = ["delete", "delc"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class DeleteCheckCommand: Runnable {

    @CommandLine.Parameters(
        index = "0",
        description = ["The name of the check to delete."]
    )
    lateinit var name: String

    override fun run() {
        if (!Application.config.checks.containsKey(name)) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check '$name' does not exist."))
            return
        }
        val file = File("check.conf/$name.conf")
        if (file.exists()) {
            file.delete()
        }
        DB.createQuery(CheckResponse::class.java).where().eq("checkName", name).delete()
        DB.createQuery(CheckMaintenance::class.java).where().eq("checkName", name).delete()
        DB.createQuery(CheckDowntime::class.java).where().eq("checkName", name).delete()
        Application.checks.remove(name)
        Application.config.checks.remove(name)
        Application.config.enabledChecks.remove(name)
        Application.configHandler.storeConfig(Application.config)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Check '$name' deleted."))
    }

}