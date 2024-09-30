package fr.infuseting.edt_app.util

import com.google.gson.JsonElement


fun filterItems(query: String, items: String): Boolean {
    if (query.isEmpty()) return true
    val upperQuery = query.uppercase().split(" ")
    val upperItems = items.uppercase().split(" ")
    return upperQuery.all { word -> upperItems.any { it.contains(word) } }
}