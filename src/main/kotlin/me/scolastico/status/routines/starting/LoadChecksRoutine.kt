package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import me.scolastico.status.dataholders.CheckConfiguration
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ConfigHandler
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi

class LoadChecksRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            print("Loading check configurations... ")
            ConsoleLoadingAnimation.enable()
            for ((name, type) in Application.config.checks) {
                if (Application.config.enabledChecks.contains(name)) for (check in Application.checkTypes) {
                    if (check.name == type) {
                        val handler = ConfigHandler(CheckConfiguration(specific = check.config), "check.conf/$name.json")
                        if (!handler.checkIfExists())
                            throw IllegalArgumentException("Check configuration file for $name does not exist!")
                        val config = handler.loadConfig()
                        if (Application.config.debug) handler.storeConfig(config)
                        Application.checks[name] = Pair(check.name, config)
                    }
                }
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
