package fr.infuseting.edt_app.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.google.gson.JsonParser
import fr.infuseting.edt_app.AdeAgenda.adeBase
import fr.infuseting.edt_app.AdeAgenda.adeRessources
import fr.infuseting.edt_app.util.PreferencesManager

@Composable
fun favDiv(navController : NavController, context : Context, paddingValues : paddingvalues) {
    PreferencesManager.getFavList(context).forEach { favItemElement ->
        val favItemJson = PreferencesManager.getFav(context, favItemElement)?.let { JsonParser.parseString(it).asJsonObject }
        if (favItemJson != null) {
            favItem(navController, context, paddingValues, favItemJson) { adeResources, adeProjectId ->
                adeBase = adeProjectId
                adeRessources = adeResources
                navController.navigate("agendaContent")
            }
        }
    }
}