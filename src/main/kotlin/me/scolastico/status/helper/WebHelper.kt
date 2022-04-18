package me.scolastico.status.helper

import io.bkbn.kompendium.core.metadata.ExceptionInfo
import io.ebean.DB
import io.github.reactivecircus.cache4k.Cache
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import me.scolastico.status.database.AverageCache
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.database.CheckResponse
import me.scolastico.status.dataholders.CheckApiData
import me.scolastico.status.dataholders.ErrorResponse
import java.time.Instant
import kotlin.reflect.typeOf
import kotlin.time.Duration.Companion.minutes

class WebHelper private constructor() {
    companion object {

        private val app = me.scolastico.status.Application

        private val averagesCacheDuration = app.config.averagesCacheDuration.toLong().minutes
        private val defaultCacheDuration = app.config.defaultCacheDuration.toLong().minutes

        private val averages = Cache.Builder().expireAfterWrite(averagesCacheDuration)
            .build<Pair<String, Int>, List<CheckApiData.AverageData>>()
        private val default = Cache.Builder().expireAfterWrite(defaultCacheDuration)
            .build<String, Triple<
                    List<CheckApiData.CheckData>,
                    List<CheckApiData.DowntimeData>,
                    List<CheckApiData.MaintenanceData>>>()

        val defaultErrorSet = setOf(
            ExceptionInfo(
                responseType = typeOf<ErrorResponse>(),
                status = HttpStatusCode.NotFound,
                description = "The check does not exist.",
                examples = mapOf(
                    "not found" to ErrorResponse(
                        error = "Check not found!",
                    ),
                )
            ),
            ExceptionInfo(
                responseType = typeOf<ErrorResponse>(),
                status = HttpStatusCode.BadRequest,
                description = "The timezone is not valid. (Only GMT offsets between -12 and 14 are valid.)",
                examples = mapOf(
                    "invalid timezone" to ErrorResponse(
                        error = "Invalid timezone!",
                    ),
                )
            )
        )

        suspend fun getData(call: ApplicationCall): CheckApiData? {
            val checkName = call.parameters["check"]!!
            if (!app.checks.containsKey(checkName)) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse(error = "Check not found!"))
                return null
            }
            val timezone = call.parameters["timezone"]!!.toIntOrNull()
            if (timezone == null || timezone > 14 || timezone < -12) {
                call.respond(HttpStatusCode.BadRequest, ErrorResponse(error = "Invalid timezone!"))
                return null
            }
            val averages = averages.get(Pair(checkName, timezone)) {
                val cache = DB.createQuery(AverageCache::class.java)
                    .where()
                    .eq("checkName", checkName)
                    .eq("timezone", timezone)
                    .findList()
                val list = mutableListOf<CheckApiData.AverageData>()
                for (entry in cache) {
                    list.add(
                        CheckApiData.AverageData(
                            duration = entry.duration,
                            uptime = entry.uptime,
                            at = entry.at.epochSecond.toString()
                        )
                    )
                }
                return@get list
            }
            val defaults = default.get(checkName) {
                val detailsHours = Instant.now().minusSeconds(app.config.detailsHours.toLong() * 60 * 60)
                val checkData = mutableListOf<CheckApiData.CheckData>()
                val checkRaw = DB.createQuery(CheckResponse::class.java)
                    .where()
                    .eq("checkName", checkName)
                    .and()
                    .gt("at", detailsHours)
                    .findList()
                for (c in checkRaw) {
                    checkData.add(
                        CheckApiData.CheckData(
                            duration = c.duration,
                            at = c.at.epochSecond.toString()
                        )
                    )
                }
                val downtimeData = mutableListOf<CheckApiData.DowntimeData>()
                val downtimeRaw = DB.createQuery(CheckDowntime::class.java)
                    .where()
                    .eq("checkName", checkName)
                    .findList()
                for (d in downtimeRaw) {
                    downtimeData.add(
                        CheckApiData.DowntimeData(
                            from = d.fromTime.epochSecond.toString(),
                            until = d.untilTime?.epochSecond?.toString(),
                            messages = d.messages
                        )
                    )
                }
                val maintenanceData = mutableListOf<CheckApiData.MaintenanceData>()
                val maintenanceRaw = DB.createQuery(CheckMaintenance::class.java)
                    .where()
                    .eq("checkName", checkName)
                    .findList()
                for (m in maintenanceRaw) {
                    maintenanceData.add(
                        CheckApiData.MaintenanceData(
                            from = m.fromTime.epochSecond.toString(),
                            until = m.untilTime?.epochSecond?.toString(),
                            message = m.message
                        )
                    )
                }
                return@get Triple(checkData, downtimeData, maintenanceData)
            }
            return CheckApiData(
                averages = averages,
                newest = defaults.first,
                downtimes = defaults.second,
                maintenances = defaults.third
            )
        }
    }
}