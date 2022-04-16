package me.scolastico.status.console

import io.ebean.DB
import me.scolastico.status.Application
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.helper.DateHelper
import org.fusesource.jansi.Ansi
import picocli.CommandLine
import picocli.CommandLine.Command

@Command(
    name = "update-downtime",
    aliases = ["ud"],
    description = ["Update the downtime status of the server."],
    mixinStandardHelpOptions = true,
    version = ["1.0.0"]
)
class UpdateDowntimeCommand: Runnable {

    @CommandLine.Option(
        names = ["-f", "--from"],
        description = ["The from date of the downtime."]
    )
    var from: String? = null

    @CommandLine.Option(
        names = ["-u", "--until"],
        description = ["The until date of the downtime."]
    )
    var until: String? = null

    @CommandLine.Option(
        names = ["-m", "--message"],
        description = ["Add an optional message to the downtime."]
    )
    var message: String? = null

    @CommandLine.Option(
        names = ["--delete-messages"],
        description = ["Delete all messages from the downtime."]
    )
    var delMessages: Boolean = false

    @CommandLine.Option(
        names = ["--dont-prefix-time"],
        description = ["Don't prefix the time to the message."]
    )
    var dontPrefixTime: Boolean = false

    @CommandLine.Option(
        names = ["--yellow"],
        description = ["Set the downtime status to yellow."]
    )
    var yellow: Boolean = false

    @CommandLine.Option(
        names = ["--red"],
        description = ["Set the downtime status to red."]
    )
    var red: Boolean = false

    @CommandLine.Parameters(
        index = "0",
        description = [
            "The id of the downtime.",
            "Only the first unique characters are needed."
        ]
    )
    lateinit var id: String

    override fun run() {
        if (from == null && until == null && message == null && !delMessages && !yellow && !red) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("You must specify at least one option."))
            return
        }
        if (message == null && dontPrefixTime) {
            println(Ansi.ansi().fgYellow().a("Warning: ").fgDefault().a("You have specified the '--dont-prefix-time' option without specifying a message."))
        }
        val query = DB.createQuery(CheckDowntime::class.java).where().startsWith("id", id)
        val count = query.findCount()
        if (count > 1) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("More than one downtime with the same id."))
            return
        }
        if (count == 0) {
            println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("No downtime with the given id."))
            return
        }
        val downtime = query.findOne()!!
        if (from != null) {
            val from = DateHelper.getDate(from!!)
            if (from == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid from date."))
                return
            }
            downtime.fromTime = from
        }
        if (until != null) {
            val until = DateHelper.getDate(until!!)
            if (until == null) {
                println(Ansi.ansi().fgRed().a("Error: ").fgDefault().a("Invalid until date."))
                return
            }
            downtime.untilTime = until
        }
        if (delMessages) {
            downtime.messages.clear()
            downtime.messages.add(Application.config.defaultDowntimeMessage)
        }
        if (message != null) {
            val time = if (dontPrefixTime) "" else "[%time%${System.currentTimeMillis()/1000}%] "
            downtime.messages.add(time + message!!)
        }
        if (yellow) {
            if (downtime.yellow) {
                println(Ansi.ansi().fgYellow().a("Warning: ").fgDefault().a("The downtime status is already yellow."))
            } else {
                downtime.yellow = true
            }
        }
        if (red) {
            if (!downtime.yellow) {
                println(Ansi.ansi().fgYellow().a("Warning: ").fgDefault().a("The downtime status is already red."))
            } else {
                downtime.yellow = false
            }
        }
        DB.save(downtime)
        println(Ansi.ansi().fgGreen().a("Success: ").fgDefault().a("Downtime updated."))
    }

}