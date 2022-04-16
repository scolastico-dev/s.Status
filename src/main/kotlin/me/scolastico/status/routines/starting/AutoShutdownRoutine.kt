package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.system.exitProcess

class AutoShutdownRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            if (Application.config.autoShutdown > 0) {
                print("Starting automatic shutdown scheduler...")
                ConsoleLoadingAnimation.enable()
                Timer("AutoShutdown").schedule(timerTask {
                    exitProcess(0)
                }, Application.config.autoShutdown*1000L)
                ConsoleLoadingAnimation.disable()
                println(Ansi.ansi().fgGreen().a("[OK]").reset())
            }
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