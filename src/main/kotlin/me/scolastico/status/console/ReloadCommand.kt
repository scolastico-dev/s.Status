package me.scolastico.status.console

import me.scolastico.status.Application
import me.scolastico.status.helper.CheckConfigurationLoader.Companion.load
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
        for ((name, data) in Application.checks) {
            for (check in Application.checkTypes) {
                if (check.name == data.first) {
                    Application.checks[name] = Pair(data.first, load(check, name))
                    break
                }
            }
        }
        println(Ansi.ansi().fgGreen().a("Configuration file reloaded.").reset())
    }

}