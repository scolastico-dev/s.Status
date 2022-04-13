package me.scolastico.status.checks

import me.scolastico.status.dataholders.CheckConfiguration
import java.net.InetAddress

class PingServerCheck: StatusCheck<PingServerCheck.PingServerConfig>  {

    override val name = "PingServer"
    override val description = "Checks if the server is reachable. (Preferred with ICMP)"
    override val config = PingServerConfig()

    override fun check(config: CheckConfiguration<PingServerConfig>) : Int {
        return ping(config.specific.ip, config.timeout)
    }

    // function to ping a server (preferred with ICMP) and return the response time or -1 if it fails
    @Suppress("TooGenericExceptionCaught")
    private fun ping(ip: String, timeout: Int): Int {
        try {
            val start = System.currentTimeMillis()
            val reachable = InetAddress.getByName(ip).isReachable(timeout * 1000)
            if (reachable) return (System.currentTimeMillis() - start).toInt()
        } catch (ignored: Exception) {}
        return -1
    }

    data class PingServerConfig(val ip: String = "93.184.216.34")

}