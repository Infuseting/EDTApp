package fr.infuseting.edt_app.ui;

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable;
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController;
import com.google.gson.JsonElement
import fr.infuseting.edt_app.util.PreferencesManager

@Composable
fun favItem(navController:NavController, context: Context, paddingValues: PaddingValues, favItem : JsonElement, update: MutableState<Boolean>, onItemClick: (Int, Int) -> Unit) {
    if (PreferencesManager.getFav(context, "${favItem.asJsonObject["adeProjectId"]?.asInt}-${favItem.asJsonObject["adeResources"]?.asInt}") != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .clickable {
                        onItemClick(
                            favItem.asJsonObject["adeResources"]?.asInt ?: 0,
                            favItem.asJsonObject["adeProjectId"]?.asInt ?: 0
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

                    Text(
                        text = "★",
                        color = Color.Yellow,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable {

                                PreferencesManager.removeFav(
                                    navController.context,
                                    "${favItem.asJsonObject["adeProjectId"]?.asInt}-${favItem.asJsonObject["adeResources"]?.asInt}"
                                )
                                navController.navigate("selectorContent")


                            }
                    )
                    Text(
                        text = favItem.asJsonObject["descTT"].asString,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }



}