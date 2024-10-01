package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

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
fun fetchMd5(url: String, callback: (String?) -> Unit) {
    val client = OkHttpClient()
    Log.d("Request Manager", "Requesting $url")
    val request = Request.Builder()
        .url(url)
        .build()
    Log.d("Request Manager", "Enqueuing request: $request")

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            e.printStackTrace()
            Log.d("Request Manager", "Request failed with exception: $e")
            callback(null)
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            Log.d("Request Manager", "Response received: $response")
            if (response.isSuccessful) {
                Log.d("Request Manager", "Request successful with code: ${response.code}")
                val jsonData = response.body?.string()
                if (jsonData != null) {
                    Log.d("Request Manager", "Response body successfully fetched: $jsonData")
                    callback(jsonData)
                } else {
                    Log.e("Request Manager", "Response body is null")
                    callback(null)
                }
            } else {
                Log.d("Request Manager", "Request failed with code: ${response.code}")
                Log.d("Request Manager", "Response message: ${response.message}")
                callback(null)
            }
        }
    })
}
fun compareMd5(md5_1: String, url: String, callback: (Boolean) -> Unit) {
    val gson = Gson()
    fetchMd5(url) { response ->
        Log.d("Response MD5 $url", response.toString())
        if (response != null) {
            val jsonObject = gson.fromJson(response, JsonElement::class.java).asJsonObject
            val md5Hash = jsonObject.get("hash")?.asString
            Log.d("Response MD5 $url", "Comparing MD5 hashes")
            Log.d("Response MD5 $url", "MD5_1: $md5_1")
            Log.d("Response MD5 $url", "MD5_2: $md5Hash")
            callback(md5_1 == md5Hash)
        } else {
            Log.e("Response MD5 $url", "Failed to fetch MD5 hash")
            callback(false)
        }
    }
}