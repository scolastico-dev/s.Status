@file:Suppress("WildcardImport")
package me.scolastico.status

import me.scolastico.status.dataholders.Config
import me.scolastico.status.routines.starting.*
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ConfigHandler
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineManager
import me.scolastico.tools.simplified.SimplifiedResourceFileReader
import me.scolastico.tools.web.WebserverManager

/**
 * Application entry point.
 */
class Application private constructor() {
    companion object {

        lateinit var web: WebserverManager
        lateinit var configHandler:ConfigHandler<Config>
        lateinit var config:Config
        val version:String = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/VERSION")
        val branch:String = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/BRANCH")
        val commit:String = SimplifiedResourceFileReader.getInstance().getStringFromResources("staticVars/COMMIT")

        /**
         * Main function of the Application.
         */
        @JvmStatic
        @Suppress("TooGenericExceptionCaught")
        fun main(args: Array<String>) {
            try {
                val routines = ArrayList<Routine>()
                routines.add(ErrorRoutine())
                routines.add(HeaderRoutine())
                routines.add(ConfigRoutine())
                routines.add(SentryRoutine())
                routines.add(LoggingRoutine())
                routines.add(DatabaseRoutine())
                routines.add(ConsoleRoutine())
                routines.add(WebRoutine())
                routines.add(FinishRoutine())
                val manager = RoutineManager(routines)
                manager.startNotAsynchronously()
            } catch (e: Exception) {
                try {
                    ConsoleLoadingAnimation.disable()
                } catch (ignored: Exception) {}
                ErrorHandler.handleFatal(e)
            }
        }

    }
}
