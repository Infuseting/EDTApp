package fr.infuseting.edt_app.util

import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.net.HttpURLConnection
import java.net.URL

val profJson = "https://edt.galade.fr/assets/json/prof.json"
val salleJson = "https://edt.galade.fr/assets/json/salle.json"
val univJson = "https://edt.galade.fr/assets/json/univ.json"


fun doRequest(url: String) : List<JsonElement> {
    val connection = URL(url).openConnection() as HttpURLConnection
    return try {
        connection.inputStream.bufferedReader().use { reader ->
            val response = reader.readText()
            Json.decodeFromString(ListSerializer(JsonElement.serializer()), response)
        }
    } finally {
        connection.disconnect()
    }

}

fun chooserJson(typeRequest: RequestType) : List<JsonElement> {
    if (typeRequest == RequestType.PROF) {
        return doRequest(profJson)
    }
    else if (typeRequest == RequestType.SALLE) {
        return doRequest(salleJson)
    }
    else if (typeRequest == RequestType.UNIV) {
        return doRequest(univJson)
    }
    return emptyList()
}
enum class RequestType {

    SALLE,
    PROF,
    UNIV
}
