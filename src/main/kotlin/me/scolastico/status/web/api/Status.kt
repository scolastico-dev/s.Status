package me.scolastico.status.web.api

import io.bkbn.kompendium.core.Notarized.notarizedGet
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.metadata.method.GetInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.scolastico.tools.web.WebserverRegistration

class Status {
    @WebserverRegistration
    fun Application.modulesStatusAPI() {
        val app = me.scolastico.status.Application
        routing {
            route("/api/status") {
                notarizedGet(
                    GetInfo<Unit, StatusResponse>(
                        summary = "Status and metadata of the application",
                        description = "The status of the application and its metadata like version, timestamp, etc.",
                        tags = setOf("General"),
                        responseInfo = ResponseInfo(
                            status = HttpStatusCode.OK,
                            description = "Default Response",
                            examples = mapOf("default" to StatusResponse(
                                "IN-DEV",
                                "IN-DEV",
                                "",
                                listOf("some check identifier"),
                                0L
                            ))
                        )
                    )
                ) {
                    call.respond(HttpStatusCode.OK, StatusResponse(
                        app.version,
                        app.commit,
                        app.branch,
                        app.config.checks.keys.toList(),
                        System.currentTimeMillis()/1000
                    ))
                }
            }
        }
    }

    data class StatusResponse(
        val version: String,
        val commit: String,
        val branch: String,
        val checks: List<String>,
        val timestamp: Long,
    )
}