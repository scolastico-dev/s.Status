package me.scolastico.status.routines.starting
import io.leego.banana.BananaUtils
import me.scolastico.status.Application
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.AnsiConsole
import java.time.Instant

/**
 * Routine for printing the current build information as the first message of the application.
 */
class HeaderRoutine : Routine{
    companion object {
        private const val CONSOLE_LOADING_SPEED = 25
    }

    @Throws(java.lang.Exception::class)
    override  fun execute(hashMap: HashMap<String, Any>): RoutineAnswer {
        return try {
            println(BananaUtils.bananaify("s.Status"))
            print("Version: " + Application.version + " | ")
            print("Commit: " + Application.branch + "/" + Application.commit + " | ")
            println("By: https://scolasti.co")
            hashMap["startingTime"] = Instant.now()
            AnsiConsole.systemInstall()
            ConsoleLoadingAnimation.setAnimation(charArrayOf('|', '/', '-', '\\'))
            ConsoleLoadingAnimation.setSpeed(CONSOLE_LOADING_SPEED)
            println()
            RoutineAnswer(hashMap)
        }catch (e: java.lang.Exception){
            me.scolastico.tools.handler.ErrorHandler.handle(e)
            RoutineAnswer(true, "exception")
        }
    }
}
