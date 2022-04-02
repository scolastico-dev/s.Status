package me.scolastico.status.checks

import me.scolastico.status.dataholders.CheckConfiguration

interface StatusCheck<C> {

    fun check(config: CheckConfiguration<C>): Int

    val name: String
    val config: C

}