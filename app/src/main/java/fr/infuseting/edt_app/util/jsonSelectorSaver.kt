package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement

fun getSavedJson(context: Context, url: String): List<JsonElement>? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("json_data", Context.MODE_PRIVATE)
    val jsonData = sharedPreferences.getString(url, null) ?: return null

    return try {
        val gson = Gson()
        val jsonElement = gson.fromJson(jsonData, JsonElement::class.java)
        when {
            jsonElement.isJsonArray -> jsonElement.asJsonArray.map { it as JsonElement }
            jsonElement.isJsonObject -> listOf(jsonElement)
            else -> emptyList()
        }
    } catch (e: Exception) {
        Log.e("Request Manager", "JSON parsing error: ${e.message}")
        null
    }
}
fun compareMd5(md5_1: String, md5_2: String): Boolean {
    return md5_1 == md5_2
}
