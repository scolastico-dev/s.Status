package me.scolastico.status.dataholders

data class CheckApiData(

    val averages: List<AverageData>,
    val newest: List<CheckData>,
    val downtimes: List<DowntimeData>,
    val maintenances: List<MaintenanceData>,

) {

    data class CheckData(
        val duration: Int,
        val at: String,
    )

    data class AverageData(
        val duration: Int,
        val uptime: Int,
        val at: String
    )

    data class DowntimeData(
        val from: String,
        val until: String?,
        val messages: List<String>
    )

    data class MaintenanceData(
        val from: String,
        val until: String?,
        val message: String,
    )

}
