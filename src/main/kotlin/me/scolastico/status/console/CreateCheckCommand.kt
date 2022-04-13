package me.scolastico.status.console

import me.scolastico.status.Application
import me.scolastico.status.dataholders.CheckConfiguration
import me.scolastico.tools.handler.ConfigHandler
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Parameters
import java.io.File

@CommandLine.Command(
    name = "create-check",
    description = ["Create a new check configuration."],
    aliases = ["create", "cc"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class CreateCheckCommand: Runnable {

    @Parameters(
        index = "0",
        description = ["The name of the check to create."]
    )
    lateinit var name: String

    @Parameters(
        index = "1",
        description = [
            "The type of the check to create.",
            "A list of available check types can be found with the command 'list-check-types'."
        ]
    )
    lateinit var type: String

    override fun run() {
        if (Application.config.checks.containsKey(name)) {
            println(
                println(
                    Ansi.ansi()
                        .fgRed().a("Error: ").fgDefault()
                        .a("A check with the name '$name' already exists.")
                )
            )
            return
        }
        for (check in Application.checkTypes) {
            if (check.name == type) {
                File("check.conf/").mkdir()
                ConfigHandler(CheckConfiguration(specific = check.config), "check.conf/$name.json").saveDefaultConfig()
                Application.config.checks[name] = check.name
                Application.configHandler.storeConfig(Application.config)
                println(
                    Ansi.ansi()
                        .fgGreen().a("Success: ").fgDefault().newline()
                        .a("Check '$name' created. You can now edit the configuration in the 'check.conf/$name.json' file.")
                        .newline().a("Please dont forget to enter 'enable-check $name' to enable the check.")
                )
                return
            }
        }
        println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Check type '$type' does not exist."))
    }
}