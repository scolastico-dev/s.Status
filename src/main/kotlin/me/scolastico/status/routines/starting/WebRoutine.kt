package me.scolastico.status.routines.starting

import io.ktor.network.tls.certificates.*
import me.scolastico.status.Application
import me.scolastico.status.web.api.Get
import me.scolastico.status.web.api.Status
import me.scolastico.status.web.etc.OpenAPI
import me.scolastico.tools.console.ConsoleLoadingAnimation
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import me.scolastico.tools.web.WebserverManager
import me.scolastico.tools.web.admin.AdminPanelInstaller
import me.scolastico.tools.web.admin.etc.KtorGsonInstaller
import org.apache.commons.lang3.RandomStringUtils
import org.fusesource.jansi.Ansi
import java.io.File

/**
 * Routine for preparing the web server.
 */
class WebRoutine: Routine {
    @Suppress("TooGenericExceptionCaught")
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        try {
            print("Preparing web server... ")
            ConsoleLoadingAnimation.enable()
            var jksPW: String = RandomStringUtils.randomAlphanumeric(32)
            var keyPW: String = RandomStringUtils.randomAlphanumeric(32)
            val conf = Application.config
            lateinit var web: WebserverManager
            if (!conf.sslDisabled) {
                val keyFile = File("keystore.jks")
                if (conf.sslKeystorePassword != null) jksPW = conf.sslKeystorePassword!!
                if (conf.sslKeyPassword != null) keyPW = conf.sslKeyPassword!!
                val cert = generateCertificate(
                    file = keyFile,
                    keyAlias = conf.sslKeyAlias,
                    keyPassword = keyPW,
                    jksPassword = jksPW
                )
                web = WebserverManager(
                    listeningPort = conf.port,
                    listeningHost = conf.host,
                    sslKeyStore = cert,
                    sslStorePassword = jksPW,
                    sslKeyPassword = keyPW,
                    sslPort = conf.port,
                    sslKeyAlias = conf.sslKeyAlias,
                    sslKeyStoreFile = keyFile
                )
            } else {
                web = WebserverManager(
                    listeningPort = conf.port,
                    listeningHost = conf.host
                )
            }
            Application.web = web
            web.registerModule(KtorGsonInstaller())
            web.registerModule(OpenAPI())
            web.registerModule(Get())
            web.registerModule(Status())
            ConsoleLoadingAnimation.disable()
            println(Ansi.ansi().fgGreen().a("[OK]").reset())
            if (Application.config.adminPanel) {
                print("Installing admin panel... ")
                ConsoleLoadingAnimation.enable()
                AdminPanelInstaller.install(web)
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