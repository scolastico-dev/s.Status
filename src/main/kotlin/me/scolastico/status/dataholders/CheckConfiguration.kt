package me.scolastico.status.dataholders

import me.scolastico.status.helper.Action
import me.scolastico.status.helper.Trigger

data class CheckConfiguration<C>(

    var displayName: String = "Display Name",
    var timeout: Int = 10,
    var every: Int = 300,
    var keep: Int = 30,
    var chartColorsResponse: MutableMap<String, String> = mutableMapOf(
        "0" to "#0E9F6E",
        "600" to "#FACA15",
        "1200" to "#C81E1E"
    ),
    var chartColorsUptime: MutableMap<String, String> = mutableMapOf(
        "0" to "#C81E1E",
        "90" to "#FACA15",
        "98" to "#0E9F6E"
    ),
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
