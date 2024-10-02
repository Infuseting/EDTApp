


package fr.infuseting.edt_app.util

import android.util.Log
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


fun getCurrentTimestamp(): String {
    val current = org.threeten.bp.LocalDateTime.now()
    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return current.format(formatter)
}
fun parseTimestampRQST(timestamp: String): org.threeten.bp.LocalDateTime {
    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'")
    return org.threeten.bp.LocalDateTime.parse(timestamp, formatter)
}
fun parseTimestampISO(timestamp: String): org.threeten.bp.LocalDateTime {
    val formatter = org.threeten.bp.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return org.threeten.bp.LocalDateTime.parse(timestamp, formatter)
}
fun compareTimestamps(timestamp1: String, timestamp2: String): Int {
    Log.d("timestamp1", timestamp1)
    Log.d("timestamp2", timestamp2)
    val time1 = parseTimestampISO(timestamp1)
    val time2 = parseTimestampRQST(timestamp2)
    return time1.compareTo(time2)
}