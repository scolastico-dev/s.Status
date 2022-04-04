package me.scolastico.status.console

import me.scolastico.status.Application
import org.fusesource.jansi.Ansi
import picocli.CommandLine

@CommandLine.Command(
    name = "reload",
    description = ["Reloads the configuration file."],
    aliases = ["r"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ReloadCommand: Runnable {

    override fun run() {
        val handler = Application.configHandler
        Application.config = handler.loadConfig()
        println(Ansi.ansi().fgGreen().a("Configuration file reloaded.").reset())
    }

}