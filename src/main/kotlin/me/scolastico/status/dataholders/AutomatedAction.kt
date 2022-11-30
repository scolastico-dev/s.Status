package me.scolastico.status.dataholders

import me.scolastico.status.helper.Action
import me.scolastico.status.helper.Trigger

data class AutomatedAction(
    val trigger: Trigger,
    val triggerData: Map<String, Any>,
    val actions: List<Pair<Action, Map<String, Any>>>
)
