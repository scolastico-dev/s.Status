package me.scolastico.status.web

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*
import me.scolastico.tools.web.WebserverRegistration
import java.io.File

class Interface {

    @WebserverRegistration
    fun Application.modulesInterface() {
        routing {
            preCompressed {
                staticRootFolder = File("web.files/")
                files(".")
                default("index.html")
            }
        }
    }

}