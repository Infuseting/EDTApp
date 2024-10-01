package fr.infuseting.edt_app.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fr.infuseting.edt_app.util.colorChooser
import fr.infuseting.edt_app.util.daySplitting
import fr.infuseting.edt_app.util.fetchAgenda
import fr.infuseting.edt_app.util.heightCalc
import fr.infuseting.edt_app.util.parseTime
import fr.infuseting.edt_app.util.topCalc
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaContent(navController: NavController, context: Context, paddingValues: PaddingValues, adeBase : Int, adeRessources : Int) {
    Column(modifier = Modifier.padding(paddingValues)) {
        TopAppBar(
            title = { Text("Agenda") },
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
            var ade_univ = "https://ade.unicaen.fr/jsp/custom/modules/plannings/anonymous_cal.jsp"
            val actualDay = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Calendar.getInstance().time)
            val Day = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 15) }.time
            )
            var result = remember { mutableStateOf("") }
            LaunchedEffect(Unit) {
                fetchAgenda(
                    context,
                    "$ade_univ?resources=$adeRessources&projectId=$adeBase&firstDate=$actualDay&lastDate=$Day",
                ) { response -> result.value = response.toString() }
            }
            var splitDay = daySplitting(result.value, actualDay)
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
                                                    fontSize = 20.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = event["LOCATION"] ?: "",
                                                    color = color.second,
                                                    modifier = Modifier.fillMaxWidth(),
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = "${parseTime(event["DTSTART"] ?: "")}' - ${parseTime(event["DTEND"] ?: "")}'",
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = color.second,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 20.sp
                                                )
                                                Text(
                                                    text = event["DESCRIPTION"]?.replace("\\n", "") ?: "",
                                                    modifier = Modifier.fillMaxWidth(),
                                                    color = color.second,
                                                    textAlign = TextAlign.Center,
                                                    fontSize = 20.sp
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