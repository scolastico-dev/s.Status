@file:Suppress("WildcardImport")
package me.scolastico.status.web.etc

import io.bkbn.kompendium.core.Kompendium
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.common.ExternalDocumentation
import io.bkbn.kompendium.oas.info.Contact
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.info.License
import io.bkbn.kompendium.oas.server.Server
import io.bkbn.kompendium.swagger.JsConfig
import io.bkbn.kompendium.swagger.SwaggerUI
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.scolastico.tools.web.WebserverRegistration
import java.net.URI

class OpenAPI {

    @WebserverRegistration
    fun Application.modulesOpenAPI() {
        val app = me.scolastico.status.Application
        install(Kompendium) {
            spec = OpenApiSpec(
                info = Info(
                    title = "s.Status",
                    version = app.version,
                    description = "s.Status is a lightweight status and server monitoring tool.",
                    license = License(
                        "MPL-2.0",
                        URI("http://github.com/scolastico-dev/s.Status/blob/main/LICENSE")
                    ),
                    contact = Contact(
                        name = "s.Status by Joschua Becker EDV",
                        url = URI("https://github.com/scolastico-dev/s.Status")
                    ),
                ),
                servers = mutableListOf(
                    Server(
                        url = URI("${if (app.config.sslDisabled) "http" else "https"}://${app.config.host}:${app.config.port}/"),
                        description = "The server from where you got this openapi spec."
                    )
                ),
                externalDocs = ExternalDocumentation(
                    URI("http://github.com/scolastico-dev/s.Status"),
                    "http://github.com/scolastico-dev/s.Status"
                )
            )
            openApiJson = { spec ->
                if (app.config.enableOpenAPI) {
                    route("/openapi.json") {
                        get {
                            call.respond(HttpStatusCode.OK, spec)
                        }
                    }
                }
            }
        }
        if (app.config.enableSwagger && app.config.enableOpenAPI) {
            install(SwaggerUI) {
                swaggerUrl = "/swagger-ui"
                jsConfig = JsConfig(mapOf(app.version to URI("/openapi.json")))
            }
        }
    }

}