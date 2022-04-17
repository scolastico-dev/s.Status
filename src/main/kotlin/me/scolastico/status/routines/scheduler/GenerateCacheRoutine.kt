package me.scolastico.status.routines.scheduler

import me.scolastico.status.Application
import me.scolastico.status.helper.CacheHelper
import me.scolastico.status.helper.DateHelper.Companion.getMidnight
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer

class GenerateCacheRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        val timezones = mutableListOf<Int>()
        val currentTime = System.currentTimeMillis()
        for (timezone in -11 .. 14) {
            val midnight = getMidnight(timezone)
            if (midnight < currentTime && currentTime - midnight < 3600000L) {
                timezones.add(timezone)
            }
        }
        for ((name, _) in Application.checks) {
            for (timezone in timezones) {
                CacheHelper.generate(name, timezone, 1)
            }
        }
        return RoutineAnswer(objectMap)
    }
}