package fr.infuseting.edt_app.util

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

fun filterItems(query: String, items: List<JsonElement>): List<JsonElement> {
    if (query.isEmpty()) return items
    val upperQuery = query.uppercase()
    return items.filter { it.toString().uppercase().contains(upperQuery) }
}