package me.scolastico.status.routines.starting

import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import me.scolastico.tools.simplified.SimplifiedResourceFileReader
import org.fusesource.jansi.Ansi
import java.io.File

/**
 * Routine for copying default web files if the "web.files" directory doesn't exist.
 */
class WebFilesRoutine: Routine {
    @Suppress("TooGenericExceptionCaught")
    override fun execute(objectMap: HashMap<String, Any>): RoutineAnswer {
        try {
            val folder = File("web.files/")
            if (!folder.exists()) {
                print("Copying default web files... ")
                ConsoleLoadingAnimation.enable()
                folder.mkdir()
                for (
                    resource in SimplifiedResourceFileReader
                        .getInstance()
                        .getAllResourcesInFolder("web")
                ) {
                    val fileName = resource.replaceFirst("web/", "")
                    val folders = fileName.split("/").dropLast(1)
                    for (folderPath in folders) {
                        val folderFile = File("web.files/$folderPath")
                        if (!folderFile.exists()) {
                            folderFile.mkdir()
                        }
                    }
                    val file = File("web.files/$fileName")
                    file.createNewFile()
                    file.writeBytes(SimplifiedResourceFileReader
                        .getInstance()
                        .getByteArrayFromResources(resource))
                }
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
