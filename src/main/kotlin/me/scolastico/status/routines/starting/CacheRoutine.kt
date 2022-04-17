package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import me.scolastico.status.helper.CacheHelper
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi

class CacheRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            print("Generating missing caches... ")
            ConsoleLoadingAnimation.enable()
            for ((name, data) in Application.checks) {
                CacheHelper.generateMissing(name, data.second.keep)
            }
            ConsoleLoadingAnimation.disable()
            println(Ansi.ansi().fgGreen().a("[OK]").reset())
        } catch (e: Exception) {
            try {
                ConsoleLoadingAnimation.disable()
            } catch (ignored: Exception) { }
            println(Ansi.ansi().fgRed().a("[FAIL]").reset())
            ErrorHandler.handle(e)
            return RoutineAnswer(true, "exception")
        }
        return RoutineAnswer(objectMap)
    }
}