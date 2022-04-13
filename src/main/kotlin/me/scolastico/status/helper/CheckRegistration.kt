package me.scolastico.status.helper

import me.scolastico.status.Application
import me.scolastico.status.checks.StatusCheck

class CheckRegistration private constructor() {
    companion object {

        fun registerCheck(check: StatusCheck<Any>): Boolean {
            for (c in Application.checkTypes) {
                if (c.name == check.name) {
                    return false
                }
            }
            Application.checkTypes.add(check)
            return true
        }

    }
}
