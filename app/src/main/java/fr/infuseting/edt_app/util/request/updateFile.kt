package fr.infuseting.edt_app.util.request

import android.content.Context
import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import com.google.gson.Gson
import com.google.gson.JsonElement
import fr.infuseting.edt_app.util.PreferencesManager
import fr.infuseting.edt_app.util.fetchAgenda
import fr.infuseting.edt_app.util.isNetworkAvailable
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
fun updateCours(context: Context,adeBase : Int, adeRessources : Int)  {
    var result = mutableStateOf("")
    var ade_univ = "https://ade.unicaen.fr/jsp/custom/modules/plannings/anonymous_cal.jsp"
    val actualDay = SimpleDateFormat(
        "yyyy-MM-dd",
        Locale.getDefault()
    ).format(Calendar.getInstance().time)
    val Day = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 15) }.time
    )
    Log.d("$adeBase-$adeRessources" ,"$ade_univ?resources=$adeRessources&projectId=$adeBase&firstDate=$actualDay&lastDate=$Day")
    fetchAgenda(
        context,
        "$ade_univ?resources=$adeRessources&projectId=$adeBase&firstDate=$actualDay&lastDate=$Day",
    ) { response ->
        result.value = response.toString()
        val timestamp = System.currentTimeMillis() / 1000L
        Log.d("$adeBase-$adeRessources", timestamp.toString())
        PreferencesManager.setStringCours(context, "$adeBase-$adeRessources", result.value)
        PreferencesManager.setLastUpdate(context, "$adeBase-$adeRessources", timestamp)
    }


}

val linkUpdate = "https://edt.galade.fr/update/"
fun needUpdate(context: Context, adeRessources: Int, adeBase: Int): Boolean{
    val result = mutableStateOf("")
    val lastUpdate = PreferencesManager.getLastUpdate(context, "$adeBase-$adeRessources")
    fetchUpdate(
        "$linkUpdate?adeBase=$adeBase&adeRessources=$adeRessources&lastUpdate=$lastUpdate&day=${PreferencesManager.getNotificationDelay(context)}"
    ) { bool ->
        result.value = bool!!
    }
    if (result.value == "") {
        return true
    }
    val jsonObject = Gson().fromJson(result.value, JsonElement::class.java).asJsonObject
    return jsonObject.get("update")?.asBoolean ?: true
}

fun fetchUpdate(url: String, callback: (String?) -> Unit) {
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

fun update(context: Context, adeBase: Int, adeRessources: Int ,callback: (Context?) -> Unit) {
    if (isNetworkAvailable(context)) {
        Log.d("$adeBase-$adeRessources", "Network Available")
        if (PreferencesManager.getStringCours(context, "$adeBase-$adeRessources") == "") {
            Log.d("$adeBase-$adeRessources", "No cache")
            needUpdate(context, adeBase, adeRessources)
            updateCours(context, adeBase, adeRessources)
        } else {
            Log.d("$adeBase-$adeRessources", "Cache")
            if (needUpdate(context, adeBase, adeRessources)) {
                Log.d("$adeBase-$adeRessources", "Need Update")
                updateCours(context, adeBase, adeRessources)
            }
        }
    }
    callback(context)
}