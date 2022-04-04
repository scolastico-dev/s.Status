package me.scolastico.status.web.api

import io.bkbn.kompendium.core.Notarized.notarizedGet
import io.bkbn.kompendium.core.metadata.ExceptionInfo
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.metadata.method.GetInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.routing.*
import me.scolastico.tools.web.WebserverRegistration
import kotlin.reflect.typeOf

class Get {
    @WebserverRegistration
    fun Application.modulesGetAPI() {
        val app = me.scolastico.status.Application
        routing {
            route("/api/get/{check}") {
                notarizedGet(
                    GetInfo<Unit, GetSuccessResponse>(
                        summary = "Get the status of a check.",
                        description = "This API provides the current information about a check.",
                        tags = setOf("General"),
                        responseInfo = ResponseInfo(
                            status = HttpStatusCode.OK,
                            description = "",
                            examples = mapOf(
                                "success" to GetSuccessResponse(
                                    data = listOf(
                                        APICheckResponse(
                                            at = 0,
                                            duration = 123,
                                            reachable = true,
                                        ),
                                        APICheckResponse(
                                            at = 100,
                                            duration = -1,
                                            reachable = false,
                                        )
                                    ),
                                )
                            )
                        ),
                        canThrow = setOf(
                            ExceptionInfo(
                                responseType = typeOf<GetErrorResponse>(),
                                status = HttpStatusCode.NotFound,
                                description = "The check does not exist.",
                                examples = mapOf(
                                    "not found" to GetErrorResponse(
                                        error = "Check not found!",
                                    ),
                                )
                            )
                        )
                    )
                ) {

                }
            }
        }
    }

    data class GetErrorResponse(
        val error: String,
    )

    data class GetSuccessResponse(
        val data: List<APICheckResponse>,
    )

    data class APICheckResponse(
        val at: Long,
        val duration: Int,
        val reachable: Boolean
    )
}