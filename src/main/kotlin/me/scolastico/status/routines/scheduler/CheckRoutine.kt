package me.scolastico.status.routines.scheduler

import io.ebean.DB
import me.scolastico.status.Application
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.database.CheckMaintenance
import me.scolastico.status.database.CheckResponse
import me.scolastico.status.database.StartedActions
import me.scolastico.status.dataholders.AutomatedAction
import me.scolastico.status.helper.ActionHelper
import me.scolastico.status.helper.Trigger
import me.scolastico.tools.handler.ErrorHandler
import me.scolastico.tools.routine.Routine
import me.scolastico.tools.routine.RoutineAnswer
import me.scolastico.tools.simplified.PBGson
import org.fusesource.jansi.Ansi
import java.time.Duration
import java.time.Instant
import kotlin.concurrent.thread

class CheckRoutine: Routine {

    override fun execute(objectMap: HashMap<String, Any>?): RoutineAnswer {
        val threads = mutableListOf<Thread>()
        for ((name, value) in Application.checks) {
            val type = value.first
            val config = value.second
            val subThread = thread(name = "CheckRoutine: $name") {
                try {
                    var count = DB.createQuery(CheckMaintenance::class.java)
                        .where()
                        .eq("checkName", name)
                        .lt("fromTime", Instant.now())
                        .isNull("untilTime")
                        .findCount()
                    count += DB.createQuery(CheckMaintenance::class.java)
                        .where()
                        .eq("checkName", name)
                        .lt("fromTime", Instant.now())
                        .gt("untilTime", Instant.now())
                        .findCount()
                    if (count > 0) {
                        return@thread
                    }
                    val lastCheckResponseCount = DB.createQuery(CheckResponse::class.java)
                        .where()
                        .eq("checkName", name)
                        .and()
                        .gt(
                            "at",
                            Instant
                                .now()
                                .minusSeconds(config.every.toLong())
                        ).findCount()
                    if (lastCheckResponseCount > 0) return@thread
                    for (checkType in Application.checkTypes) if (checkType.name == type) {
                        try {
                            val duration = checkType.check(config)
                            if (duration < 0) {
                                handleFailResponse(name, config.actions)
                            } else {
                                handleSuccessResponse(duration, name, config.actions)
                            }
                        } catch (e: Exception) {
                            ErrorHandler.handle(e)
                        }
                        return@thread
                    }
                    println(Ansi.ansi().fgRed().a("[ERROR]").fgDefault().a(" Check type '$type' not found!"))
                } catch (e: Exception) {
                    ErrorHandler.handle(e)
                }
            }
            threads.add(subThread)
        }
        for (thread in threads) {
            thread.join()
        }
        return RoutineAnswer(objectMap)
    }

    private fun handleSuccessResponse(duration: Int, name: String, actions: List<AutomatedAction>) {
        val checkResponse = CheckResponse(name, duration)
        DB.save(checkResponse)
        val downtimeQuery = DB.createQuery(CheckDowntime::class.java)
            .where()
            .eq("checkName", name)
            .and()
            .isNull("untilTime")
        val downtimeCount = downtimeQuery.findCount()
        if (downtimeCount > 0) for (action in actions) when (action.trigger) {
            Trigger.DOWNTIME_OFFLINE_AGAIN -> {
                DB.createQuery(StartedActions::class.java)
                    .where()
                    .eq("checkName", name)
                    .and()
                    .eq("action", PBGson.s().toJson(action))
                    .delete()
            }
            Trigger.DOWNTIME_ONLINE_AGAIN -> {
                if (action.triggerData.containsKey("THRESHOLD")) {
                    val threshold = try {
                        (action.triggerData["THRESHOLD"] as Double).toLong()
                    } catch (e: Exception){
                        println(Ansi.ansi().fgRed().a("[ERROR]").fgDefault().a(" Invalid threshold for an action of the check '${name}'!"))
                        continue
                    }
                    val lastDowntime = DB.createQuery(CheckResponse::class.java)
                        .where()
                        .eq("checkName", name)
                        .and()
                        .lt("duration", 0)
                        .orderBy("at desc")
                        .setMaxRows(1)
                        .findOne()
                    if (lastDowntime == null) {
                        println(Ansi.ansi().fgRed().a("[ERROR]").fgDefault()
                            .a(" No downtime found for check '$name'!")
                            .a(" Because of this the 'DOWNTIME_ONLINE_AGAIN' action")
                            .a(" with an 'THRESHOLD' can't be executed!"))
                        continue
                    }
                    val time = Duration.between(lastDowntime.at, Instant.now()).seconds
                    if (time < threshold) continue
                }
                ActionHelper.runIfNotInDB(name, action)
            }
        }
    }

    private fun handleFailResponse(name: String, actions: List<AutomatedAction>) {
        val checkResponse = CheckResponse(name, -1)
        DB.save(checkResponse)
        val downtimeQuery = DB.createQuery(CheckDowntime::class.java)
            .where()
            .eq("checkName", name)
            .and()
            .isNull("untilTime")
        val downtimeCount = downtimeQuery.findCount()
        if (downtimeCount > 1) {
            println(Ansi.ansi().fgYellow().a("[WARNING]").fgDefault().a(" More than one active downtime found for check '$name'!"))
        }
        if (downtimeCount > 0) for (action in actions) when (action.trigger) {
            Trigger.DOWNTIME_OFFLINE_AGAIN -> {
                ActionHelper.runIfNotInDB(name, action)
            }
            Trigger.DOWNTIME_ONLINE_AGAIN -> {
                DB.createQuery(StartedActions::class.java)
                    .where()
                    .eq("checkName", name)
                    .and()
                    .eq("action", PBGson.s().toJson(action))
                    .delete()
            }
        }
        if (downtimeCount == 0) {
            val downtime = CheckDowntime(name, Instant.now())
            DB.save(downtime)
        }
    }

}