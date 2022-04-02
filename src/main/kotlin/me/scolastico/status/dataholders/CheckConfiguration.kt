package me.scolastico.status.dataholders

data class CheckConfiguration<T>(

    var timeout: Int = 30,
    var specific: T,

)
