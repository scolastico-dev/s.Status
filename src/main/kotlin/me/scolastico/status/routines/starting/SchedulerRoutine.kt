package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import me.scolastico.tools.routine.RoutineManager
import org.fusesource.jansi.Ansi
import java.util.*
import kotlin.concurrent.timerTask

class SchedulerRoutine: Routine {

    private var running = false

    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            print("Starting scheduler... ")
            ConsoleLoadingAnimation.enable()
            Timer("s.Status Main Scheduler").scheduleAtFixedRate(timerTask {
                if (!running) {
                    running = true
                    val manager = RoutineManager(Application.schedulerRoutines)
                    manager.startNotAsynchronously()
                    running = false
                }
            }, 10*1000, 1000)
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