package me.scolastico.status.web.api

import io.bkbn.kompendium.core.Notarized.notarizedGet
import io.bkbn.kompendium.core.metadata.ParameterExample
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.metadata.method.GetInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.scolastico.status.dataholders.CheckApiData
import me.scolastico.status.dataholders.ErrorResponse
import me.scolastico.status.helper.WebHelper
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.simplified.PBGson
import me.scolastico.tools.web.WebserverRegistration

class Get {

    @WebserverRegistration
    fun Application.modulesGetAPI() {
        routing {
            route("/api/get/{check}/{timezone}") {
                notarizedGet(
                    GetInfo<Unit, GetResponse>(
                        summary = "Get the status of a check.",
                        description = "This API provides the current information about a check.",
                        tags = setOf("General"),
                        parameterExamples = setOf(
                            ParameterExample(
                                parameterName = "check",
                                exampleName = "The check internal name/id to get the status of.",
                                exampleValue = "example"
                            ),
                            ParameterExample(
                                parameterName = "timezone",
                                exampleName = "The timezone to use for the response. (The GMT offset is used.)",
                                exampleValue = "1"
                            )
                        ),
                        responseInfo = ResponseInfo(
                            status = HttpStatusCode.OK,
                            description = "",
                            examples = mapOf(
                                "success" to GetResponse(
                                    data = CheckApiData(
                                        averages = listOf(),
                                        newest = listOf(),
                                        downtimes = listOf(),
                                        maintenances = listOf(),
                                    ),
                                    chartColorsResponse = mapOf(
                                        "0" to "#0E9F6E",
                                        "600" to "#FACA15",
                                        "1200" to "#C81E1E"
                                    ),
                                    chartColorsUptime = mapOf(
                                        "0" to "#C81E1E",
                                        "90" to "#FACA15",
                                        "98" to "#0E9F6E"
                                    ),
                                    timestamp = 0L
                                )
                            )
                        ),
                        canThrow = WebHelper.defaultErrorSet
                    )
                ) {
                    try {
                        val response = WebHelper.getData(call)
                        if (response != null) {
                            val checkName = call.parameters["check"]!!
                            val check = me.scolastico.status.Application.checks[checkName]!!.second
                            val json = PBGson.s().toJson(GetResponse(
                                data = response,
                                chartColorsResponse = check.chartColorsResponse,
                                chartColorsUptime = check.chartColorsUptime,
                                timestamp = System.currentTimeMillis()/1000
                            ))
                            call.respond(HttpStatusCode.OK, json)
                        }
                    } catch (e: Exception) {
                        ErrorHandler.handle(e)
                        call.respond(
                            HttpStatusCode.InternalServerError,
                            ErrorResponse(
                                error = "Unknown error!"
                            )
                        )
                    }
                }
            }
        }
    }

    data class GetResponse(
        val data: CheckApiData,
        var chartColorsResponse: Map<String, String>,
        var chartColorsUptime: Map<String, String>,
        val timestamp: Long,
    )

}