package fr.infuseting.edt_app.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.infuseting.edt_app.util.PreferencesManager
import fr.infuseting.edt_app.util.colorChooser
import fr.infuseting.edt_app.util.daySplitting
import fr.infuseting.edt_app.util.formatDateFromTimestamp
import fr.infuseting.edt_app.util.heightCalc
import fr.infuseting.edt_app.util.parseTime
import fr.infuseting.edt_app.util.request.update
import fr.infuseting.edt_app.util.topCalc
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaContent(navController: NavController, context: Context, paddingValues: PaddingValues, adeBase : Int, adeRessources : Int) {
    var AgendaTime = remember { mutableStateOf("Agenda | ${formatDateFromTimestamp(PreferencesManager.getLastUpdate(context, "$adeBase-$adeRessources"))}") }
    Column(modifier = Modifier.padding(paddingValues)) {
        TopAppBar(
            title = { Text(text=AgendaTime.value.toString()) },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("selectorContent") }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.Blue)
        ) {
            var continuer = remember { mutableStateOf(true)}
            var isNetwork = remember { mutableStateOf(true) }
            LaunchedEffect(Unit) {
                continuer.value = true
                update(context, adeBase, adeRessources) { context ->
                    if (PreferencesManager.getStringCours(context!!, "$adeBase-$adeRessources") == null) {
                        isNetwork.value = false
                    }
                    Log.d("update", "done")
                    continuer.value = false
                    AgendaTime.value = "Agenda | ${formatDateFromTimestamp(PreferencesManager.getLastUpdate(context, "$adeBase-$adeRessources"))}"
                }
            }

            if (continuer.value) {
                // Afficher un indicateur de chargement ou retourner pour arrêter l'exécution
                return@Row
            }
            if (isNetwork.value == false) {
                Text(
                    text = "Aucune connexion internet et aucune donnée actuellement sauvegardé",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.White
                )
                return@Row
            }
            else {
                val response = PreferencesManager.getStringCours(context, "$adeBase-$adeRessources")!!
                val actualDay = SimpleDateFormat(
                    "yyyy-MM-dd",
                    Locale.getDefault()
                ).format(Calendar.getInstance().time)
                Log.d("$adeBase-$adeRessources", "$response")
                var splitDay = daySplitting(response, actualDay)

                if (splitDay.isNotEmpty()) {
                    Log.d("splitDay", splitDay.toString())
                    LazyRow(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                        items(16) { i ->
                            val dayEvents = splitDay[i]
                            if (dayEvents.isNotEmpty()) {
                                val date = Calendar.getInstance().apply {
                                    time = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
                                        actualDay
                                    )!!
                                    add(Calendar.DAY_OF_YEAR, i)
                                }.time

                                val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                                val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
                                val dayName = dayFormat.format(date)
                                val formattedDate = dateFormat.format(date)

                                Column(
                                    modifier = Modifier.background(Color.LightGray)
                                        .padding(8.dp)


                                ) {

                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .align(Alignment.CenterHorizontally)
                                    ) {
                                        Text(
                                            text = if (i == 0) "Aujourd'hui" else dayName,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            color = Color.Black,
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = formattedDate,
                                            modifier = Modifier.fillMaxWidth(),
                                            textAlign = TextAlign.Center,
                                            color = Color.Black,
                                            fontSize = 18.sp,
                                        )
                                    }
                                    var timeLast = Calendar.getInstance().apply {
                                        set(Calendar.HOUR_OF_DAY, 6)
                                        set(Calendar.MINUTE, 0)
                                        set(Calendar.SECOND, 0)
                                    }.time
                                    Column(modifier = Modifier.background(Color.White).height(1440.dp)) {
                                        dayEvents.forEach { event ->

                                            val color = colorChooser(event)
                                            val heightCalc = heightCalc(event)
                                            val results = topCalc(event, timeLast)
                                            timeLast = results.second
                                            val topCalc = results.first

                                            Column(modifier = Modifier.padding(top = if (topCalc > 0) topCalc.dp else 0.dp)) {
                                                Column(

                                                    modifier = Modifier
                                                        .background(color.first)
                                                        .border(1.dp, Color.Black)
                                                        .fillParentMaxWidth()
                                                        .height(heightCalc.dp)
                                                ) {
                                                    Text(
                                                        text = event["SUMMARY"] ?: "",
                                                        color = color.second,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center,
                                                        fontSize = 16.sp,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                    Text(
                                                        text = event["LOCATION"] ?: "",
                                                        color = color.second,
                                                        modifier = Modifier.fillMaxWidth(),
                                                        textAlign = TextAlign.Center,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = "${parseTime(event["DTSTART"] ?: "")}' - ${parseTime(event["DTEND"] ?: "")}'",
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = color.second,
                                                        textAlign = TextAlign.Center,
                                                        fontSize = 14.sp
                                                    )
                                                    Text(
                                                        text = event["DESCRIPTION"]?.replace("\\n", "") ?: "",
                                                        modifier = Modifier.fillMaxWidth(),
                                                        color = color.second,
                                                        textAlign = TextAlign.Center,
                                                        fontSize = 14.sp
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


        }

    }



}