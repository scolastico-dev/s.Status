package me.scolastico.status.web.api

import io.bkbn.kompendium.core.Notarized.notarizedGet
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.metadata.method.GetInfo
import io.ktor.application.*
import io.ktor.http.*
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
                        summary = "",
                        description = "",
                        tags = setOf("General"),
                        responseInfo = ResponseInfo(
                            status = HttpStatusCode.OK,
                            description = "",
                            examples = mapOf()
                        )
                    )
                ) {

                }
            }
        }
    }

    data class StatusResponse(
        val version: String,
        val commit: String,
        val checks: List<String>,
    )
}