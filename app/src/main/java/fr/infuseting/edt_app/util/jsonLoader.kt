package fr.infuseting.edt_app.util

import android.content.Context
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Salle(val nameSalle: String, val adeProjectId: String, val adeResources: String)

@Serializable
data class Prof(val nameProf: String, val adeProjectId: String, val adeResources: String)

@Serializable
data class Univ(val nameUniv: String, val timetable: List<Timetable>)

@Serializable
data class Timetable(val adeProjectId: String, val adeResources: String, val descTT: String)

fun loadSalleData(context: Context): List<Salle> {
    val jsonString = context.assets.open("salle.json").bufferedReader().use { it.readText() }
    return Json.decodeFromString(jsonString)
}

fun loadProfData(context: Context): List<Prof> {
    val jsonString = context.assets.open("prof.json").bufferedReader().use { it.readText() }
    return Json.decodeFromString(jsonString)
}

fun loadUnivData(context: Context): List<Univ> {
    val jsonString = context.assets.open("univ.json").bufferedReader().use { it.readText() }
    return Json.decodeFromString(jsonString)
}