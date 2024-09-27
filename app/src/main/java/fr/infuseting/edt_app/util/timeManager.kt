


package fr.infuseting.edt_app.util

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


fun getCurrentTimestamp(): String {
    val current = org.threeten.bp.LocalDateTime.now()
    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return current.format(formatter)
}
fun parseTimestamp(timestamp: String): org.threeten.bp.LocalDateTime {
    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return org.threeten.bp.LocalDateTime.parse(timestamp, formatter)
}
fun compareTimestamps(timestamp1: String, timestamp2: String): Int {
    val time1 = parseTimestamp(timestamp1)
    val time2 = parseTimestamp(timestamp2) as org.threeten.bp.chrono.ChronoLocalDateTime<*>
    return time1.compareTo(time2)
}