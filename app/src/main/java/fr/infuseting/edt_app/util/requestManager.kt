package fr.infuseting.edt_app.util

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


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
fun fetchAgenda(context: Context, url: String, callback: (String?) -> Unit) {
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

                        callback(jsonData)

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
    val savedJsonStr = (savedJson.toString().replace("\n", "").removeSurrounding("[", "]"))
    compareMd5(getMD5EncryptedString(savedJsonStr), url.replace("json/", "json/?fileName=")) { isSame ->
        if (isSame ) {
            Log.d("Request Manager", "Using saved JSON data")
            callback(savedJson)
        } else {
            Log.d("Request Manager", "Fetching JSON data")
            if (isNetworkAvailable(context)) {
                fetchJsonAsList(context, url, callback)
            } else {
                Log.e("Request Manager", "No internet connection")
                callback(savedJson)
            }
        }
    }
}
fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}
enum class RequestType {

    SALLE,
    PROF,
    UNIV
}

fun getMD5EncryptedString(encTarget: String): String {
    var mdEnc: MessageDigest? = null
    try {
        mdEnc = MessageDigest.getInstance("MD5")
    } catch (e: NoSuchAlgorithmException) {
        println("Exception while encrypting to md5")
        e.printStackTrace()
    } // Encryption algorithm

    mdEnc!!.update(encTarget.toByteArray(), 0, encTarget.length)
    var md5 = BigInteger(1, mdEnc.digest()).toString(16)
    while (md5.length < 32) {
        md5 = "0$md5"
    }
    return md5
}