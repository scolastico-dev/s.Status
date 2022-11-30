package me.scolastico.status.routines.starting

import me.scolastico.status.Application
import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.ConsoleAppender
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import org.fusesource.jansi.Ansi
import org.slf4j.LoggerFactory

class LoggingRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            print("Configuring log level... ")
            ConsoleLoadingAnimation.enable()
            val debug = Application.config.debug
            val rootLogger = LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME) as Logger
            if (debug) {
                rootLogger.level = Level.DEBUG
            } else {
                rootLogger.level = Level.INFO
                val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
                val layoutEncoder = PatternLayoutEncoder()
                layoutEncoder.pattern = Ansi.ansi()
                    .fgYellow().a("%level ").fgGreen()
                    .a("%class ").fgDefault()
                    .a("%msg%n").toString()
                layoutEncoder.context = loggerContext
                layoutEncoder.start()
                val logConsoleAppender = ConsoleAppender<ILoggingEvent>()
                logConsoleAppender.context = loggerContext
                logConsoleAppender.name = "console"
                logConsoleAppender.encoder = layoutEncoder
                logConsoleAppender.start()
                rootLogger.detachAndStopAllAppenders()
                rootLogger.addAppender(logConsoleAppender)
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
