package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi
import java.time.Duration
import java.time.Instant

/**
 * Routine for printing last message of the startup routine flow.
 */
class FinishRoutine : Routine {

    @Throws(Exception::class)
    @Suppress("TooGenericExceptionCaught")
    override fun execute(hashMap: HashMap<String, Any>): RoutineAnswer {
        return try {
            val startingTime = hashMap["startingTime"] as Instant
            @Suppress("MagicNumber")
            val startingDuration = Duration.between(startingTime, Instant.now()).toMillis() / 1000.0
            println()
            println("Done! Starting took $startingDuration seconds.")
            println()
            if (!Application.config.sentry) {
                warn()
                warn("SENTRY DISABLED WARNING")
                warn()
                warn("You disabled our sentry error reporting!")
                warn("We need this to monitor and fix bugs.")
                warn("It would be really nice if you re- enable that setting.")
                warn()
                warn("Also did you know that we do that anonymously, dont")
                warn("share any data with third party's, dont collect any")
                warn("usage information about you and your pc and host our own")
                warn("sentry server so we dont need to share data with other")
                warn("company's? We only want the bugs! ༼ つ ◕_◕ ༽つ")
                warn()
                warn("Read more here: https://go.scolasti.co/sentry")
                warn()
                println()
            }
            ConsoleManager.enable()
            Application.web.start()
            RoutineAnswer(hashMap)
        } catch (e: Exception) {
            try {
                ConsoleLoadingAnimation.disable()
            } catch (ignored: Exception) {}
            ErrorHandler.handle(e)
            RoutineAnswer(true, "exception")
        }
    }

    private fun warn(message: String = "") {
        println(Ansi.ansi().fgRed().a("WARNING: $message").fgDefault())
    }
}
