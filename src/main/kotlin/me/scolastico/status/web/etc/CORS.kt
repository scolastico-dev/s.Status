package me.scolastico.status.web.etc

import io.ktor.application.*
import io.ktor.http.*
import me.scolastico.tools.web.WebserverRegistration

class CORS {

    @WebserverRegistration
    fun Application.modulesCORS() {
        val app = me.scolastico.status.Application
        val list = app.config.cors
        install(io.ktor.features.CORS) {
            if (list.size == 1 && list[0] == "*") {
                anyHost()
            } else {
                list.forEach {
                    host(it)
                }
            }
            methods.remove(HttpMethod.Post)
        }
    }

}