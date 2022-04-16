package me.scolastico.status.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.util.*

class DateHelper private constructor() {
    companion object {

        var dateFormats = mutableListOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss'Z'",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss'Z'",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS",
            "yyyy-MM-dd'T'HH:mm:ss",
            "yyyy-MM-dd HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd HH:mm:ss'Z'",
            "yyyy-MM-dd HH:mm:ss.SSS",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd'T'HH:mm:ss.SS",
            "dd-MM-yyyy HH:mm:ss.SSS'Z'",
            "dd-MM-yyyy HH:mm:ss'Z'",
            "dd-MM-yyyy HH:mm:ss.SSS",
            "dd-MM-yyyy HH:mm:ss",
            "dd-MM-yyyy'T'HH:mm:ss.SSS'Z'",
            "dd-MM-yyyy'T'HH:mm:ss'Z'",
            "dd-MM-yyyy'T'HH:mm:ss.SSS",
            "dd-MM-yyyy'T'HH:mm:ss",
            "dd.MM.yyyy HH:mm:ss.SSS'Z'",
            "dd.MM.yyyy HH:mm:ss'Z'",
            "dd.MM.yyyy HH:mm:ss.SSS",
            "dd.MM.yyyy HH:mm:ss",
            "dd.MM.yyyy'T'HH:mm:ss.SSS'Z'",
            "dd.MM.yyyy'T'HH:mm:ss'Z'",
            "dd.MM.yyyy'T'HH:mm:ss.SSS",
            "dd.MM.yyyy'T'HH:mm:ss",
        )

        fun getDate(date: String): Instant? {
            if (date == "now") {
                return Instant.now()
            }
            if (date.toLongOrNull() != null) {
                return Instant.ofEpochMilli(date.toLong())
            }
            for (format in dateFormats) {
                try {
                    val df = SimpleDateFormat(format, Locale.US)
                    df.isLenient = false
                    return df.parse(date).toInstant()!!
                } catch (ignored: ParseException) {}
            }
            return null
        }

        fun getDuration(start: Instant, end: Instant): String {
            val duration = Duration.between(start, end)
            val hours = duration.toHours()
            val minutes = duration.minusHours(hours).toMinutes()
            val seconds = duration.minusHours(hours).minusMinutes(minutes).toSeconds()
            return "$hours:$minutes:$seconds"
        }

        fun getUTCTime(instant: Instant): String {
            val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            df.timeZone = TimeZone.getTimeZone("UTC")
            return df.format(Date.from(instant))
        }

    }
}