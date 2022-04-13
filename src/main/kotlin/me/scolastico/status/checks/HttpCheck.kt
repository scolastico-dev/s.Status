package me.scolastico.status.checks

import io.ktor.http.*
import me.scolastico.status.dataholders.CheckConfiguration
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class HttpCheck: StatusCheck<HttpCheck.HttpCheckConfig> {

    override val name = "HTTP"
    override val description = "Checks if a HTTP endpoint is reachable."
    override val config = HttpCheckConfig()

    override fun check(config: CheckConfiguration<HttpCheckConfig>): Int {
        return checkUrl(config.timeout, config.specific)
    }

    // function to check if an url with the data from HttpCheckConfig is reachable
    @Suppress("TooGenericExceptionCaught")
    private fun checkUrl(timeout: Int, data: HttpCheckConfig): Int {
        try {
            val currentTime = System.currentTimeMillis()
            val url = URL(data.url)
            val connection = if (data.url.startsWith("https://")) {
                url.openConnection() as HttpsURLConnection
            } else {
                url.openConnection() as HttpURLConnection
            }
            connection.requestMethod = data.method.value
            connection.connectTimeout = timeout
            connection.readTimeout = timeout
            for (header in data.headers) {
                connection.setRequestProperty(header.key, header.value)
            }
            if (data.body.isNotEmpty() && data.method != HttpMethod.Get) {
                connection.doOutput = true
                connection.outputStream.write(data.body.toByteArray())
            }
            connection.connect()
            if (connection.responseCode == data.expectedStatusCode) {
                return (System.currentTimeMillis() - currentTime).toInt()
            }
        } catch (ignored: Exception) {}
        return -1
    }

    data class HttpCheckConfig(
        val url: String = "https://example.com/",
        val method: HttpMethod = HttpMethod.Get,
        val headers: Map<String, String> = mapOf(),
        val body: String = "",
        val expectedStatusCode: Int = 200,
    )

}