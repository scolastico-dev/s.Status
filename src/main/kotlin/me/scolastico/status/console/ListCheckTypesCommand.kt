package me.scolastico.status.console

import me.scolastico.status.Application
import me.scolastico.tools.etc.TableGeneratorThemes
import picocli.CommandLine.Command

@Command(
    name = "list-check-types",
    description = ["Lists all available check types."],
    aliases = ["lct"],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class ListCheckTypesCommand: Runnable {
    override fun run() {
        val table = TableGeneratorThemes.FANCY_BOARDER()
            .addContent("Check Type", "Description")
            .addTableLineSeparator()
        for (check in Application.checkTypes) {
            table.addContent(check.name, check.description)
        }
        println(table)
    }
}