package me.scolastico.status.helper

import io.ebean.DB
import me.scolastico.status.Application
import me.scolastico.status.database.CheckDowntime
import me.scolastico.status.database.StartedActions
import me.scolastico.status.dataholders.AutomatedAction
import me.scolastico.tools.simplified.PBGson
import org.fusesource.jansi.Ansi
import java.time.Instant
import java.util.*
import kotlin.concurrent.timerTask

class ActionHelper private constructor() {
    companion object {

        fun run(name: String, actionData: AutomatedAction) {
            for (action in actionData.actions) {
                when (action.first) {
                    Action.DOWNTIME_YELLOW -> {
                        val downtime = getDowntime(name) ?: return
                        if (!downtime.yellow) {
                            downtime.yellow = true
                            DB.save(downtime)
                        }
                    }
                    Action.DOWNTIME_RED -> {
                        val downtime = getDowntime(name) ?: return
                        if (downtime.yellow) {
                            downtime.yellow = false
                            DB.save(downtime)
                        }
                    }
                    Action.CLOSE_DOWNTIME -> {
                        val downtime = getDowntime(name) ?: return
                        downtime.untilTime = Instant.now()
                        DB.save(downtime)
                        val clearTriggers = listOf(
                            Trigger.DOWNTIME_ONLINE_AGAIN,
                            Trigger.DOWNTIME_ONLINE_AGAIN
                        )
                        val otherActions = Application.checks[name]!!.second.actions
                        Timer("Cleanup for $name").schedule(timerTask {
                            for (a in otherActions) if (clearTriggers.contains(a.trigger)) {
                                DB.createQuery(StartedActions::class.java)
                                    .where()
                                    .eq("checkName", name)
                                    .and()
                                    .eq("action", PBGson.s().toJson(a))
                                    .delete()
                            }
                        }, 10*1000)
                    }
                }
            }
        }

        fun runIfNotInDB(name: String, action: AutomatedAction) {
            val count = DB.createQuery(StartedActions::class.java)
                .where()
                .eq("checkName", name)
                .and()
                .eq("action", PBGson.s().toJson(action))
                .findCount()
            if (count == 0) {
                DB.save(StartedActions(name, PBGson.s().toJson(action)))
                run(name, action)
            }
        }

        private fun getDowntime(name: String): CheckDowntime? {
            val query = DB.createQuery(CheckDowntime::class.java)
                .where()
                .eq("checkName", name)
                .and()
                .isNull("untilTime")
            val count = query.findCount()
            if (count > 1) {
                println(
                    Ansi.ansi().fgRed().a("[ERROR]").fgDefault()
                    .a(" More than one downtime entry found for $name!")
                    .a(" Because of this, some actions are not executed!"))
                return null
            }
            if (count == 0) {
                println(
                    Ansi.ansi().fgRed().a("[ERROR]").fgDefault()
                        .a(" No downtime entry found for $name!")
                        .a(" Because of this, some actions are not executed!"))
                return null
            }
            return query.findOne()
        }

    }
}