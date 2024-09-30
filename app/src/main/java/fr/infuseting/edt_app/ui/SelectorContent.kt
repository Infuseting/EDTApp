package fr.infuseting.edt_app.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import fr.infuseting.edt_app.AdeAgenda.adeBase
import fr.infuseting.edt_app.AdeAgenda.adeRessources
import fr.infuseting.edt_app.util.PreferencesManager
import fr.infuseting.edt_app.util.RequestType
import fr.infuseting.edt_app.util.chooserJson
import kotlinx.coroutines.delay

@Composable
fun SelectorContent(navController: NavController, context: Context, paddingValues: PaddingValues) {

    val searchQuery = remember { mutableStateOf("") }
    var salleItems by remember { mutableStateOf<List<JsonElement>?>(null) }
    var profItems by remember { mutableStateOf<List<JsonElement>?>(null) }
    var univItems by remember { mutableStateOf<List<JsonElement>?>(null) }

    LaunchedEffect(Unit) {
        chooserJson(context, RequestType.SALLE) { items -> salleItems = items }
        chooserJson(context, RequestType.PROF) { items -> profItems = items }
        chooserJson(context, RequestType.UNIV) { items -> univItems = items }
    }


    Column(modifier = Modifier.padding(paddingValues)) {
        Text(
            text = "DESIGN EN COURS DE CREATION ! CECI EST UNE VERSION BETA !",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "ATTENTION LES COURS SUPPLEMENTAIRE NE SONT PAS NECESSAIREMENT ECRIT SUR L'EMPLOI DU TEMPS",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(searchQuery)
        Spacer(modifier = Modifier.height(16.dp))
        LaunchedEffect(salleItems, profItems, univItems) {
            Log.d("MainContent", "SalleItems: $salleItems")
            Log.d("MainContent", "ProfItems: $profItems")
            Log.d("MainContent", "UnivItems: $univItems")
        }
        var update = remember { mutableStateOf(false) }
        LazyColumn {
            item{

                PreferencesManager.getFavList(context).forEach { favItemElement ->
                    val favItemJson = PreferencesManager.getFav(context, favItemElement)?.let { JsonParser.parseString(it).asJsonObject }
                    if (favItemJson != null) {
                        favItem(navController, context, paddingValues, favItemJson, update) { adeResources, adeProjectId ->
                            adeBase = adeProjectId
                            adeRessources = adeResources
                            navController.navigate("agendaContent")
                        }
                    }
                }
            }
            item {
                salleItems?.forEach { salleItem ->

                    DropdownMenu(
                        searchQuery,
                        context,
                        navController,
                        title = "Salle",
                        items = salleItem.asJsonObject["salle"],
                        RequestType.SALLE,
                        update

                    ){ adeResources, adeProjectId ->
                        adeBase = adeProjectId
                        adeRessources = adeResources
                        navController.navigate("agendaContent")
                    }
                }


            }
            item {
                profItems?.forEach { profItem ->
                    DropdownMenu(
                        searchQuery,
                        context,
                        navController,
                        title = "Professeur",
                        items = profItem.asJsonObject["prof"],
                        RequestType.PROF,
                        update
                    )
                    { adeResources, adeProjectId ->
                        adeBase = adeProjectId
                        adeRessources = adeResources
                        navController.navigate("agendaContent")
                    }
                }
            }

            univItems?.forEach { univItem ->
                val univArray = univItem.asJsonObject["univ"]?.asJsonArray
                if (univArray != null) {
                    univArray.forEach { univElement ->
                        item {

                            Log.d("Dropdown Menu", "UnivElement: $univElement")
                            DropdownMenu(
                                searchQuery,
                                context,
                                navController,
                                title = univElement.asJsonObject["nameUniv"]?.asString ?: "Unknown University",
                                items = univElement.asJsonObject["timetable"] ?: JsonObject(),
                                RequestType.UNIV,
                                update,
                            ) { adeResources, adeProjectId ->
                                adeBase = adeProjectId
                                adeRessources = adeResources
                                navController.navigate("agendaContent")
                            }
                        }
                    }
                } else {
                    Log.d("Dropdown Menu", "Univ is not a JSON array")
                }
            }
        }


    }
}