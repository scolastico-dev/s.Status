package me.scolastico.status.routines.scheduler

import me.scolastico.status.Application
import me.scolastico.status.helper.CacheHelper
import me.scolastico.status.helper.DateHelper
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer

class GenerateCacheRoutine: Routine {
    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        val timezones = mutableMapOf<Int, Int>()
        val currentTime = System.currentTimeMillis()
        for (day in -1..1) {
            for (timezone in -11 .. 14) {
                val midnight = DateHelper.getMidnight(timezone) - (day * 86400000L)
                if (midnight < currentTime && currentTime - midnight < 3600000L) {
                    timezones[timezone] = day + 1
                }
            }
        }
        for ((name, _) in Application.checks) {
            for ((timezone, day) in timezones) {
                CacheHelper.generate(name, timezone, day)
            }
        }
        return RoutineAnswer(objectMap)
    }
}