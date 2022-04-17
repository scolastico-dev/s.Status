package me.scolastico.status.routines.scheduler

import io.ebean.DB
import me.scolastico.status.Application
import me.scolastico.status.database.AverageCache
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.database.CheckResponse
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import java.time.Instant

class CleanupRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        for ((name, value) in Application.checks) {
            try {
                val time = Instant.now()
                    .minusSeconds(value.second.keep.toLong() * 60 * 60 * 24)
                DB.createQuery(CheckResponse::class.java)
                    .where()
                    .eq("checkName", name)
                    .and()
                    .lt("at", time)
                    .delete()
                DB.createQuery(CheckDowntime::class.java)
                    .where()
                    .eq("checkName", name)
                    .and()
                    .isNotNull("untilTime")
                    .and()
                    .lt("untilTime", time)
                    .delete()
                DB.createQuery(CheckMaintenance::class.java)
                    .where()
                    .eq("checkName", name)
                    .and()
                    .isNotNull("untilTime")
                    .and()
                    .lt("untilTime", time)
                    .delete()
                DB.createQuery(AverageCache::class.java)
                    .where()
                    .eq("checkName", name)
                    .and()
                    .lt("at", time.minusSeconds(86400000L*2))
                    .delete()
            } catch (e: Exception) {
                ErrorHandler.handle(e)
            }
        }
        return RoutineAnswer(objectMap)
    }
}