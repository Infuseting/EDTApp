package fr.infuseting.edt_app.util

import android.util.Log
import androidx.compose.material3.DatePicker
import androidx.compose.ui.graphics.Color
import java.text.DateFormat
import java.time.LocalDateTime



fun colorChooser(element : Map<String, String>): Pair<Color, Color> {
    val currentTimeStamp = getCurrentTimestamp()
    val endClass = element["DTEND"].toString()
    if (compareTimestamps(currentTimeStamp, endClass) > 0) {
        return Pair(Color(0xFFA9A9A9), Color.Black)
    }
    Log.d("element", element["SUMMARY"].toString())
    return when {
        element["SUMMARY"].toString().contains("CC") -> Pair(Color(0xFFFF6347), Color.White)
        element["SUMMARY"].toString().contains("CTP") -> Pair(Color(0xFFFF6347), Color.White)
        element["SUMMARY"].toString().contains("CTD") -> Pair(Color(0xFFFF6347), Color.White)
        element["SUMMARY"].toString().contains("TP") -> Pair( Color(0xFF32CD32), Color.White)
        element["SUMMARY"].toString().contains("TD") -> Pair( Color(0xFF1E90FF), Color.White)
        element["SUMMARY"].toString().contains("CM") -> Pair( Color(0xFFFFD700), Color.Black)
        element["DESCRIPTION"].toString().contains("CC") -> Pair(Color(0xFFFF6347), Color.White)
        element["DESCRIPTION"].toString().contains("CTP") -> Pair(Color(0xFFFF6347), Color.White)
        element["DESCRIPTION"].toString().contains("CTD") -> Pair(Color(0xFFFF6347), Color.White)
        element["DESCRIPTION"].toString().contains("TP") -> Pair(Color(0xFF32CD32), Color.White)
        element["DESCRIPTION"].toString().contains("TD") -> Pair(Color(0xFF1E90FF), Color.White)
        element["DESCRIPTION"].toString().contains("CM") -> Pair(Color(0xFFFFD700), Color.Black)
        else -> Pair(Color(0xFF00CED1), Color.Black)
    }
}