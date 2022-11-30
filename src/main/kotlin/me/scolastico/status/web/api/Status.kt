package me.scolastico.status.web.api

import io.bkbn.kompendium.core.Notarized.notarizedGet
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.metadata.method.GetInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.scolastico.status.helper.StatusString
import me.scolastico.status.helper.WebHelper
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
                                listOf(CheckInformation(
                                    "some check identifier",
                                    30,
                                    StatusString.ONLINE,
                                    "fancy example name"
                                )),
                                60,
                                0L
                            ))
                        )
                    )
                ) {
                    val checks = mutableListOf<CheckInformation>()
                    for ((checkName, data) in app.checks) checks.add(CheckInformation(
                        name = checkName,
                        keep = data.second.keep,
                        status = WebHelper.getStatus(checkName),
                        displayName = data.second.displayName
                    ))
                    call.respond(HttpStatusCode.OK, StatusResponse(
                        app.version,
                        app.commit,
                        app.branch,
                        checks,
                        app.config.refreshInterval,
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
        val checks: List<CheckInformation>,
        val refreshInterval: Int,
        val timestamp: Long,
    )

    data class CheckInformation(
        val name: String,
        val keep: Int,
        val status: StatusString,
        val displayName: String,
    )

}