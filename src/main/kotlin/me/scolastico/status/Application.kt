@file:Suppress("WildcardImport")
package me.scolastico.status

import me.scolastico.status.checks.StatusCheck
import me.scolastico.status.dataholders.CheckConfiguration
import me.scolastico.status.dataholders.Config
import me.scolastico.status.routines.scheduler.CheckRoutine
import me.scolastico.status.routines.scheduler.CleanupRoutine
import me.scolastico.status.routines.scheduler.GenerateCacheRoutine
import me.scolastico.status.routines.starting.*
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ConfigHandler
import me.scolastico.tools.handler.ErrorHandler
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
        var checkTypes = mutableListOf<StatusCheck<Any>>()
        var checks = mutableMapOf<String, Pair<String, CheckConfiguration<Any>>>()
        var schedulerRoutines = mutableListOf(CheckRoutine(), CleanupRoutine(), GenerateCacheRoutine())
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
                val manager = RoutineManager(listOf(
                    ErrorRoutine(),
                    HeaderRoutine(),
                    ConfigRoutine(),
                    SentryRoutine(),
                    LoggingRoutine(),
                    RegisterChecksRoutine(),
                    LoadChecksRoutine(),
                    WebFilesRoutine(),
                    DatabaseRoutine(),
                    CacheRoutine(),
                    ConsoleRoutine(),
                    WebRoutine(),
                    SchedulerRoutine(),
                    AutoShutdownRoutine(),
                    FinishRoutine()
                ))
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
