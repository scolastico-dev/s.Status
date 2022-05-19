package me.scolastico.status.dataholders

import me.scolastico.status.helper.Action
import me.scolastico.status.helper.Trigger

data class CheckConfiguration<C>(

    var displayName: String = "Display Name",
    var timeout: Int = 10,
    var every: Int = 300,
    var keep: Int = 30,
    var actions: List<AutomatedAction> = listOf(
        AutomatedAction(
            Trigger.DOWNTIME_ONLINE_AGAIN,
            mapOf(
                "THRESHOLD" to 3600.0,
            ),
            listOf(
                Pair(Action.CLOSE_DOWNTIME, mapOf())
            )
        ),
        AutomatedAction(
            Trigger.DOWNTIME_ONLINE_AGAIN,
            mapOf(
                "THRESHOLD" to 600.0,
            ),
            listOf(
                Pair(Action.DOWNTIME_YELLOW, mapOf())
            )
        ),
        AutomatedAction(
            Trigger.DOWNTIME_OFFLINE_AGAIN,
            mapOf(),
            listOf(
                Pair(Action.DOWNTIME_RED, mapOf())
            )
        )
    ),
    var specific: C,

)
