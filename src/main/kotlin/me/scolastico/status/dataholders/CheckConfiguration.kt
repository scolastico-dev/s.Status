package me.scolastico.status.dataholders

data class CheckConfiguration<C>(

    var timeout: Int = 10,
    var every: Int = 300,
    var specific: C,

)
