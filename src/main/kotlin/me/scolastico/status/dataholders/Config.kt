package me.scolastico.status.dataholders

/**
 * The main configuration data class of the application.
 */
data class Config(

    var port: Int = 42010,
    var host: String = "localhost",
    var enableSwagger: Boolean = false,
    var enableOpenAPI: Boolean = false,
    var sentry: Boolean = true,
    var sslKeystorePassword: String? = null,
    var sslKeyPassword: String? = null,
    var sslKeyAlias: String = "ssl",
    var sslDisabled: Boolean = false,
    var adminPanel: Boolean = false,
    var debug: Boolean = false,
    var autoShutdown: Int = 0,
    var defaultDowntimeMessage: String = "Detected issues with this service.",
    var cors: List<String> = listOf("*"),
    var checks: MutableMap<String, String> = mutableMapOf(),
    var enabledChecks: MutableList<String> = mutableListOf(),

)
