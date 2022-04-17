package me.scolastico.status.helper

import io.ebean.DB
import me.scolastico.status.database.AverageCache
import me.scolastico.status.database.CheckResponse
import me.scolastico.status.helper.DateHelper.Companion.getMidnight
import java.time.Instant

class CacheHelper private constructor() {
    companion object {

        fun generateMissing(name: String, days: Int) {
            val currentTime = System.currentTimeMillis()
            for (day in 1 .. days) for (timezone in -11 .. 14) {
                val endTime = getMidnight(timezone) - (day * 86400000L) + 86400000
                if (endTime > currentTime) continue
                val count = DB.createQuery(AverageCache::class.java)
                    .where()
                    .eq("checkName", name)
                    .eq("at", endTime)
                    .eq("timezone", timezone)
                    .findCount()
                if (count == 0) {
                    generate(name, timezone, day)
                }
            }
        }

        fun generate(name: String, timezone: Int, day: Int) {
            val startTime = getMidnight(timezone) - (day * 86400000L)
            val endTime = Instant.ofEpochMilli(startTime + 86400000L)
            if (endTime > Instant.now()) return
            DB.createQuery(AverageCache::class.java)
                .where()
                .eq("checkName", name)
                .eq("at", endTime)
                .eq("timezone", timezone)
                .delete()
            val list = DB.createQuery(CheckResponse::class.java)
                .where()
                .eq("checkName", name)
                .between(
                    "at",
                    Instant.ofEpochMilli(startTime),
                    endTime
                )
                .findList()
            var averageDuration = 0.0
            var reachable = 0.0
            for (response in list) if (response.duration > 0) {
                averageDuration += response.duration
                reachable++
            }
            if (averageDuration > 0) averageDuration /= list.size
            if (reachable > 0) reachable = reachable * 100 / list.size
            val cache = AverageCache(
                name,
                reachable.toInt(),
                averageDuration.toInt(),
                endTime,
                timezone
            )
            DB.save(cache)
        }

    }
}