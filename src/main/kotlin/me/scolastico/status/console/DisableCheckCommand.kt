package me.scolastico.status.console

import me.scolastico.status.Application
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Command

@Command(
    name = "disable-check",
    description = ["Disable a check"],
    mixinStandardHelpOptions = true,
    aliases = ["disable", "dc"],
    version = ["1.0.0"],
)
class DisableCheckCommand: Runnable {

    @CommandLine.Parameters(
        index = "0",
        description = ["The name of the check to disable."]
    )
    lateinit var name: String

    override fun run() {
        if (!Application.checks.containsKey(name)) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check not found."))
            return
        }
        Application.checks.remove(name)
        Application.config.enabledChecks.remove(name)
        Application.configHandler.storeConfig(Application.config)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Check disabled."))
    }

}