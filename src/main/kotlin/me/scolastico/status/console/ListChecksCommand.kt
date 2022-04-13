package me.scolastico.status.console

import me.scolastico.status.Application
import me.scolastico.tools.etc.TableGeneratorThemes
import picocli.CommandLine.Command

@Command(
    name = "list-checks",
    description = ["List all checks"],
    aliases = ["list", "lc"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ListChecksCommand: Runnable {

    override fun run() {
        val enabled = TableGeneratorThemes.FANCY_BOARDER()
            .addContent("Name", "Type", "File")
            .addTableLineSeparator()
        val disabled = TableGeneratorThemes.FANCY_BOARDER()
            .addContent("Name", "Type", "File")
            .addTableLineSeparator()
        for ((check, type) in Application.config.checks) {
            if (Application.config.enabledChecks.contains(check)) {
                enabled.addContent(check, type, "check.conf/$check.json")
            } else {
                disabled.addContent(check, type, "check.conf/$check.json")
            }
        }
        println("Enabled checks:")
        println(enabled)
        println("Disabled checks:")
        println(disabled)
    }

}