package fr.infuseting.edt_app.ui

import android.content.Context
import android.util.Log
import android.view.PixelCopy.Request
import androidx.collection.mutableIntSetOf
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import fr.infuseting.edt_app.R
import fr.infuseting.edt_app.util.PreferencesManager
import fr.infuseting.edt_app.util.RequestType
import fr.infuseting.edt_app.util.filterItems
import kotlin.math.log

@Composable
fun DropdownMenu(
    searchQuery: MutableState<String>,
    context : Context,
    navController: NavController,
    title: String,
    items: JsonElement,
    type: RequestType,
    update: MutableState<Boolean>,
    onItemClick: (Int, Int) -> Unit
) {

    var lengthSearchQuery by remember { mutableStateOf(1) }
    var expanded by remember { mutableStateOf(false) }
    if ((type != RequestType.UNIV || ((searchQuery.value.isEmpty() || filterItems(searchQuery.value ,title ))))) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Red)
                        .clickable { expanded = !expanded }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_play),
                            contentDescription = "Settings",
                            modifier = Modifier.rotate(if (expanded) 90f else 0f)
                        )
                        Text(
                            text = title,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                if (lengthSearchQuery != searchQuery.value.length) {
                    if (searchQuery.value.length > 3) {
                        lengthSearchQuery = searchQuery.value.length
                        expanded = true
                    }
                    else {
                        expanded = false
                        lengthSearchQuery = searchQuery.value.length
                    }
                }


                if (expanded) {
                    LazyColumn(
                        horizontalAlignment = Alignment.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 192.dp)
                    ) {
                        items(items.asJsonArray.size()) { index ->
                            val item = items.asJsonArray[index]
                            val tri = when (type) {
                                RequestType.SALLE -> item.asJsonObject["descTT"]?.asString ?: "Unknown Salle"
                                RequestType.PROF -> item.asJsonObject["descTT"]?.asString ?: "Unknown Prof"
                                RequestType.UNIV -> item.asJsonObject["descTT"]?.asString ?: "Unknown Univ"
                                else -> "Unknown"
                            }
                            if (type == RequestType.UNIV || searchQuery.value.isEmpty() || filterItems(searchQuery.value, tri)) {
                                Box(
                                    modifier = Modifier
                                        .clickable {
                                            onItemClick(
                                                item.asJsonObject["adeResources"]?.asInt ?: 0,
                                                item.asJsonObject["adeProjectId"]?.asInt ?: 0
                                            )
                                        }
                                        .fillMaxWidth()
                                        .background(Color.Blue)
                                        .border(1.dp, Color.Black)
                                        .padding(16.dp),
                                    contentAlignment = Alignment.TopEnd
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {

                                        var color by remember { mutableStateOf(
                                            if (PreferencesManager.getFav(context, "${item.asJsonObject["adeProjectId"]?.asInt}-${item.asJsonObject["adeResources"]?.asInt}") != null) Color.Yellow else Color.Black
                                        ) }

                                        Text(
                                            text = "★",
                                            color = color,
                                            modifier = Modifier
                                                .padding(end = 8.dp)
                                                .clickable {
                                                    if (PreferencesManager.getFav(context, "${item.asJsonObject["adeProjectId"]?.asInt}-${item.asJsonObject["adeResources"]?.asInt}") != null) {
                                                        PreferencesManager.removeFav(
                                                            navController.context,
                                                            "${item.asJsonObject["adeProjectId"]?.asInt}-${item.asJsonObject["adeResources"]?.asInt}"
                                                        )
                                                    } else {
                                                        PreferencesManager.addFav(
                                                            navController.context,
                                                            "${item.asJsonObject["adeProjectId"]?.asInt}-${item.asJsonObject["adeResources"]?.asInt}",
                                                            item.toString()
                                                        )
                                                    }
                                                    navController.navigate("selectorContent")
                                                    Log.d("Fav", "favItem: ${PreferencesManager.getFavList(context)}")
                                                    color = if (PreferencesManager.getFav(context, "${item.asJsonObject["adeProjectId"]?.asInt}-${item.asJsonObject["adeResources"]?.asInt}") != null) Color.Yellow else Color.Black
                                                }
                                        )
                                        Text(
                                            text = tri,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }


                }
            }
        }
    }


}
