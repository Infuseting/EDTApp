package fr.infuseting.edt_app.util

import androidx.compose.material3.DatePicker
import androidx.compose.ui.graphics.Color
import java.text.DateFormat
import java.time.LocalDateTime



fun colorChooser(element : Map<String, String>): Color {
    val currentTimeStamp = getCurrentTimestamp()
    val endClass = element["DTEND"].toString()
    if (compareTimestamps(currentTimeStamp, endClass) < 0) {
        return Color(0xFFA9A9A9)
    }
    return when (element["SUMMARY"]) {
        "CC" -> Color(0xFFFF6347)
        "CTP" -> Color(0xFFFF6347)
        "CTD" -> Color(0xFFFF6347)
        "TP" -> Color(0xFF32CD32)
        "TD" -> Color(0xFF1E90FF)
        "CM" -> Color(0xFFFFD700)
        else -> return when (element["DESCRIPTION"]) {
            "CC" -> Color(0xFFFF6347)
            "CTP" -> Color(0xFFFF6347)
            "CTD" -> Color(0xFFFF6347)
            "TP" -> Color(0xFF32CD32)
            "TD" -> Color(0xFF1E90FF)
            "CM" -> Color(0xFFFFD700)
            else -> Color(0xFF00CED1)
        }
    }
}