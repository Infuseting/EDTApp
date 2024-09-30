package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import okhttp3.*
import java.io.IOException

val profJson = "https://edt.galade.fr/assets/json/prof.json"
val salleJson = "https://edt.galade.fr/assets/json/salle.json"
val univJson = "https://edt.galade.fr/assets/json/univ.json"


fun fetchJsonAsList(context: Context, url: String, callback: (List<JsonElement>?) -> Unit) {
    val client = OkHttpClient()
    Log.d("Request Manager", "Requesting $url")
    val request = Request.Builder()
        .url(url)
        .build()
    Log.d("Request Manager", "Enqueuing request: $request")

    client.newCall(request).enqueue(object : Callback {
        init {
            Log.d("Request Manager", "Request callback initialized")
        }
        override fun onFailure(call: Call, e: IOException) {
            e.printStackTrace()
            Log.d("Request Manager", "Request failed with exception: $e")
            callback(null) // Return null on failure
        }

        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                Log.d("Request Manager", "Request successful")
                val jsonData = response.body?.string()

                if (jsonData != null) {
                    try {
                        // Save JSON data to SharedPreferences
                        val sharedPreferences: SharedPreferences = context.getSharedPreferences("json_data", Context.MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString(url, jsonData)
                            apply()
                        }

                        // Parse the JSON string to a list of JsonElement using Gson
                        val gson = Gson()
                        val jsonElement = gson.fromJson(jsonData, JsonElement::class.java)
                        val jsonList = when {
                            jsonElement.isJsonArray -> jsonElement.asJsonArray.map { it as JsonElement }
                            jsonElement.isJsonObject -> listOf(jsonElement)
                            else -> emptyList()
                        }

                        // Pass the parsed list to the callback
                        callback(jsonList)
                    } catch (e: Exception) {
                        Log.e("Request Manager", "JSON parsing error: ${e.message}")
                        callback(null)
                    }
                } else {
                    Log.e("Request Manager", "Response body is null")
                    callback(null)
                }
            } else {
                Log.d("Request Manager", "Request failed with code: ${response.code}")
                callback(null) // Return null on failure
            }
        }
    })
    Log.d("Request Manager", "Request sent")
}

fun chooserJson(context: Context, typeRequest: RequestType, callback: (List<JsonElement>?) -> Unit) {
    val url = when (typeRequest) {
        RequestType.PROF -> profJson
        RequestType.SALLE -> salleJson
        RequestType.UNIV -> univJson
    }

    val savedJson = getSavedJson(context, url)
    if (savedJson != null || compareMd5(savedJson.toString(), url)) {
        Log.d("Request Manager", "Using saved JSON data")
        callback(savedJson)

    } else {
        Log.d("Request Manager", "Fetching JSON data")
        fetchJsonAsList(context, url, callback)
    }
}
enum class RequestType {

    SALLE,
    PROF,
    UNIV
}
