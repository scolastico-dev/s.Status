package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi

/**
 * Routine for enabling sentry error reporting.
 */
class SentryRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>): RoutineAnswer {
        print("Enabling sentry error reporting... ")
        if (Application.config.sentry) {
            if (Application.version != "IN-DEV") {
                ErrorHandler.enableSentry(
                    "https://2d378b8fef18468ea967f975720ae8ca@sentry.scolasti.co/2",
                    1.0,
                    Application.version
                )
                println(Ansi.ansi().fgGreen().a("[OK]").reset())
            } else {
                println(Ansi.ansi().fgYellow().a("[IN-DEV]").reset())
            }
        } else {
            println(Ansi.ansi().fgYellow().a("[DISABLED]").reset())
        }
        return RoutineAnswer(objectMap)
    }
}