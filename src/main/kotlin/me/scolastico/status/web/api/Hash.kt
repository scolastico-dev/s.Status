package me.scolastico.status.web.api

import io.bkbn.kompendium.core.Notarized.notarizedGet
import io.bkbn.kompendium.core.metadata.ParameterExample
import io.bkbn.kompendium.core.metadata.ResponseInfo
import io.bkbn.kompendium.core.metadata.method.GetInfo
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import me.scolastico.status.dataholders.ErrorResponse
import me.scolastico.status.helper.WebHelper
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.simplified.PBGson
import me.scolastico.tools.web.WebserverRegistration
import java.math.BigInteger
import java.security.MessageDigest

class Hash {

    @WebserverRegistration
    fun Application.modulesGetAPI() {
        routing {
            route("/api/hash/{check}/{timezone}") {
                notarizedGet(
                    GetInfo<Unit, HashResponse>(
                        summary = "Get only the hash from the response of an '/api/get/{check}/{timezone}' call.",
                        description = "This API provides the hash of the response from the '/api/get/{check}/{timezone}' api." +
                                "This api is usefully to test if the response has changed or is still the same.",
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
                                "success" to HashResponse(
                                    hash = "example-hash-ABCDEFGHIJKLMNOPQRSTUVWXYZ",
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
                            val json = PBGson.s().toJson(response)
                            val md5 = MessageDigest.getInstance("MD5").digest(json.toByteArray())
                            val hash = BigInteger(1, md5).toString(16)
                            call.respond(HttpStatusCode.OK, HashResponse(
                                hash,
                                System.currentTimeMillis()/1000
                            ))
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

    data class HashResponse(
        val hash: String,
        val timestamp: Long
    )

}