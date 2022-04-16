package me.scolastico.status.helper

import com.google.gson.Gson
import me.scolastico.status.Application
import me.scolastico.status.checks.StatusCheck
import me.scolastico.status.dataholders.CheckConfiguration
import me.scolastico.tools.handler.ConfigHandler
import org.apache.commons.io.FileUtils
import org.json.JSONObject
import java.io.File
import java.nio.charset.StandardCharsets

class CheckConfigurationLoader private constructor() {
    companion object {
        fun load(check: StatusCheck<Any>, name: String): CheckConfiguration<Any> {
            val handler = ConfigHandler(CheckConfiguration(specific = check.config), "check.conf/$name.json")
            if (!handler.checkIfExists())
                throw IllegalArgumentException("Check configuration file for $name does not exist!")
            val config = handler.loadConfig()
            val content = FileUtils.readFileToString(File("check.conf/$name.json"), StandardCharsets.UTF_8)
            val json = JSONObject(content)
            config.specific = Gson().fromJson(json.getJSONObject("specific").toString(), check.config::class.java)
            if (Application.config.debug) handler.storeConfig(config)
            return config
        }
    }
}