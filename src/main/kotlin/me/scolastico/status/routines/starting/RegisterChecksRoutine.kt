package me.scolastico.status.routines.starting

import me.scolastico.status.checks.HttpCheck
import me.scolastico.status.checks.PingPortCheck
import me.scolastico.status.checks.PingServerCheck
import me.scolastico.status.checks.StatusCheck
import me.scolastico.status.helper.CheckRegistration
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi

class RegisterChecksRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            print("Registering check types... ")
            ConsoleLoadingAnimation.enable()
            CheckRegistration.registerCheck(HttpCheck() as StatusCheck<Any>)
            CheckRegistration.registerCheck(PingPortCheck() as StatusCheck<Any>)
            CheckRegistration.registerCheck(PingServerCheck() as StatusCheck<Any>)
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