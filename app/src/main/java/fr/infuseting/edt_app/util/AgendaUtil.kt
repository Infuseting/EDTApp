package fr.infuseting.edt_app.util

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.threeten.bp.format.DateTimeFormatter
import java.time.LocalDate

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

fun daySplitting(response: String, actualDay: String): MutableList<MutableList<Map<String, String>>> {

    val list = List(16) { mutableListOf<Map<String, String>>() }
    val timeDay = mutableMapOf<String, Int>()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    for (i in 0 until 16) {
        val calendar = Calendar.getInstance().apply {
            time = dateFormat.parse(actualDay)!!
            add(Calendar.DAY_OF_YEAR, i)
        }
        timeDay[dateFormat.format(calendar.time)] = i
    }

    val events = response.split("BEGIN:VEVENT")
    for ((index, value) in events.withIndex()) {
        if (index == 0) continue // Skip the first split part if it's not an event
        val eventData = parseEventData(value)
        val startTimeDay = dateFormat.format(SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault()).parse(eventData["DTSTART"]!!)!!)
        list[timeDay[startTimeDay]!!].add(eventData)
    }
    for (i in 0 until 16) {
        list[i].sortWith(compareBy({ it["DTSTART"] }))
    }
    return list.toMutableList()
}
fun parseTime(times: String): String {
    val formatter = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault())
    val parsedTime = formatter.parse(times)
    val outputFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val calendar = Calendar.getInstance().apply {
        time = parsedTime!!
        add(Calendar.HOUR, 2)
    }
    return outputFormatter.format(calendar.time)
}
fun parseEventData(event: String): Map<String, String> {
    val data = event.trim().lines()
    val eventData = mutableMapOf<String, String>()
    var tempKey = ""

    for (line in data) {
        val cleanLine = line.split("(Exporté le:")[0]
        if (":" in cleanLine) {
            val (key, value) = cleanLine.split(":", limit = 2)
            tempKey = key.trim()
            eventData[tempKey] = value.trim()
        } else {
            eventData[tempKey] = eventData[tempKey] + " " + cleanLine.trim()
        }
    }
    return eventData
}

fun heightCalc(event: Map<String, String>): Int {
    val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(parseTime(event["DTSTART"]!!))!!
    val endTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(parseTime(event["DTEND"]!!))!!
    val duration = ((endTime.time - startTime.time) / (1000 * 60)).toInt()
    return (duration * 2F).toInt()
}

fun topCalc(event: Map<String, String>, i: Date): Pair<Int, Date> {
    val startTime = SimpleDateFormat("HH:mm", Locale.getDefault()).parse(parseTime(event["DTSTART"]!!))!!
    val calendar = Calendar.getInstance().apply {
        time = startTime
        add(Calendar.HOUR, -8)
    }
    val hour = calendar.get(Calendar.HOUR_OF_DAY)
    val minute = calendar.get(Calendar.MINUTE)
    val calendarLastCoursEnd = Calendar.getInstance().apply {
        time = i
        add(Calendar.HOUR, -6)
    }
    val hourLastCoursEnd = calendarLastCoursEnd.get(Calendar.HOUR_OF_DAY)
    val minuteLastCoursEnd = calendarLastCoursEnd.get(Calendar.MINUTE)
    Log.d("hour", hour.toString() + " " + minute.toString())
    Log.d("hourLastCoursEnd", hourLastCoursEnd.toString() + " " + minuteLastCoursEnd.toString())
    val difEndStart = ((hourLastCoursEnd * 60 + minuteLastCoursEnd) * 2F).toInt()
    val paddingTop = ((hour * 60 + minute) * 2F).toInt()
    Log.d("paddingTop", paddingTop.toString())
    Log.d("difEndStart", difEndStart.toString())
    val newI = SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.getDefault()).parse(event["DTEND"]!!)!!
    return Pair(paddingTop - difEndStart, newI)
}