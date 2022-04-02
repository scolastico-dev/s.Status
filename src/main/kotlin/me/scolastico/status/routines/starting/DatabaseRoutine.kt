package me.scolastico.status.routines.starting

import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.ebean.DatabaseConfig
import me.scolastico.tools.ebean.DatabaseConnector
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi

/**
 * Routine for connecting to the database and running the migrations.
 */
class DatabaseRoutine : Routine {
    @Throws(Exception::class)
    @Suppress("TooGenericExceptionCaught")
    override fun execute(hashMap: HashMap<String, Any>): RoutineAnswer {
        return try {
            val config = hashMap["dbConfig"] as DatabaseConfig?
            val connector = DatabaseConnector(true)
            print("Loading database drivers... ")
            ConsoleLoadingAnimation.enable()
            DatabaseConnector.loadDatabaseDrivers()
            ConsoleLoadingAnimation.disable()
            println(Ansi.ansi().fgGreen().a("[OK]").reset())
            print("Connecting to database... ")
            ConsoleLoadingAnimation.enable()
            connector.connectToDatabase(config)
            ConsoleLoadingAnimation.disable()
            println(Ansi.ansi().fgGreen().a("[OK]").reset())
            print("Migrate database... ")
            ConsoleLoadingAnimation.enable()
            connector.runMigrations()
            ConsoleLoadingAnimation.disable()
            println(Ansi.ansi().fgGreen().a("[OK]").reset())
            RoutineAnswer(hashMap)
        } catch (e: Exception) {
            try {
                ConsoleLoadingAnimation.disable()
            } catch (ignored: Exception) {
            }
            println(Ansi.ansi().fgRed().a("[FAIL]").reset())
            ErrorHandler.handle(e)
            RoutineAnswer(true, "exception")
        }
    }
}
