package me.scolastico.status.checks

import me.scolastico.status.dataholders.CheckConfiguration
import java.net.InetSocketAddress
import java.net.Socket


class PingPortCheck: StatusCheck<PingPortCheck.PingPortConfig> {

    override val name = "PingPort"
    override val config = PingPortConfig()

    override fun check(config: CheckConfiguration<PingPortConfig>) : Int {
        return ping(config.specific.ip, config.specific.port, config.timeout)
    }

    // function to ping a server and port and return the response time or -1 if it fails
    @Suppress("TooGenericExceptionCaught")
    private fun ping(host: String, port: Int, timeout: Int): Int {
        return try {
            val start = System.currentTimeMillis()
            val socket = Socket()
            socket.connect(InetSocketAddress(host, port), timeout*1000)
            socket.close()
            (System.currentTimeMillis() - start).toInt()
        } catch (e: Exception) {
            -1
        }
    }

    data class PingPortConfig(val ip: String = "example.com", val port: Int = 80)

}