package me.scolastico.status.routines.starting

import me.scolastico.status.console.ReloadCommand
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.console.ConsoleManager
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi

/**
 * Routine for registering all core console commands.
 */
class ConsoleRoutine: Routine {
    @Suppress("TooGenericExceptionCaught")
    override fun execute(objectMap: HashMap<String, Any>): RoutineAnswer {
        try {
            print("Registering console commands... ")
            ConsoleLoadingAnimation.enable()
            ConsoleManager.registerCommand(ReloadCommand())
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
