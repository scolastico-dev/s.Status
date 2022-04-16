package me.scolastico.status.console

import me.scolastico.status.Application
import me.scolastico.status.helper.CheckConfigurationLoader.Companion.load
import org.fusesource.jansi.Ansi
import picocli.CommandLine

@CommandLine.Command(
    name = "enable-check",
    description = ["Enables a check configuration"],
    aliases = ["enable", "ec"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class EnableCheckCommand: Runnable {

    @CommandLine.Parameters(
        index = "0",
        description = ["The name of the check to enable."]
    )
    lateinit var name: String

    override fun run() {
        if (Application.checks.containsKey(name)) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check already enabled."))
            return
        }
        if (!Application.config.checks.containsKey(name)) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check not found."))
            return
        }
        var found = false
        for (check in Application.checkTypes) {
            if (check.name == Application.config.checks[name]!!) {
                try {
                    Application.checks[name] = Pair(check.name, load(check, name))
                    found = true
                } catch (e: IllegalArgumentException) {
                    println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a(e.message))
                }
            }
        }
        if (!found) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check type not found."))
            return
        }
        Application.config.enabledChecks.add(name)
        Application.configHandler.storeConfig(Application.config)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Check enabled."))
    }

}