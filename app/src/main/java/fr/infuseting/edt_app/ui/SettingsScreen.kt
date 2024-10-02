package fr.infuseting.edt_app.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.infuseting.edt_app.util.PreferencesManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController, onDarkModeToggle: (Boolean) -> Unit) {
    val context = LocalContext.current
    var darkModeEnabled by remember { mutableStateOf(PreferencesManager.isDarkModeEnabled(context) ?: false) }
    var notificationDelay by remember { mutableStateOf(PreferencesManager.getNotificationDelay(context)) }

    Column(modifier = Modifier.padding(16.dp)) {
        TopAppBar(
            title = { Text("Settings") },
            navigationIcon = {
                IconButton(onClick = { navController.navigate("selectorContent") }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Dark Mode")
            Switch(
                checked = darkModeEnabled,
                onCheckedChange = {
                    darkModeEnabled = it
                    PreferencesManager.setDarkModeEnabled(context, it)
                    onDarkModeToggle(it)
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Notification Delay (days)")
        Slider(
            value = notificationDelay.toFloat(),
            onValueChange = {
                notificationDelay = it.toInt()
                PreferencesManager.setNotificationDelay(context, notificationDelay)
            },
            valueRange = 1f..10f,
            steps = 9
        )

        Text(
            text = "Maximum: $notificationDelay day(s)\nThis parameter corresponds to the maximum number of days you can receive a notification in case of a course change.",
            modifier = Modifier.padding(vertical = 8.dp)
        )
        var rangeTime by remember { mutableStateOf(PreferencesManager.getStartTime(context).toFloat()..PreferencesManager.getEndTime(context).toFloat()) }

        Text(text = "Define Start and End time for notifications")
        Text(text = "Average data consumption per month : ${(31.0*((PreferencesManager.getEndTime(context)-PreferencesManager.getStartTime(context))/(PreferencesManager.getRequestPerMinute(context)/60.0))).toInt()}KB")
        Text(text = "Notification start and end : ${rangeTime.start.toInt()}h - ${rangeTime.endInclusive.toInt()}h")
        RangeSlider(
            value = rangeTime,
            onValueChange = { range ->
                rangeTime = range
                PreferencesManager.setStartTime(context, range.start.toInt())
                PreferencesManager.setEndTime(context, range.endInclusive.toInt())
            },
            valueRange = 0f..24f,
            steps = 23
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Time between two request in minute")

        var requestPerMinute by remember { mutableStateOf(PreferencesManager.getRequestPerMinute(context)) }
        Text("Time : ${requestPerMinute.toInt()} minute")
        Slider(
            value = requestPerMinute.toFloat(),
            onValueChange = {
                requestPerMinute = it.toInt()
                PreferencesManager.setRequestPerMinute(context, requestPerMinute)
            },
            valueRange = 1f..240f,
            steps = 15
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Visit infuseting.github.io",
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://infuseting.github.io"))
                    navController.context.startActivity(intent)
                }
                .padding(vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Thanks to Antonin Huaut for their help",
            modifier = Modifier
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://antoninhuaut.fr/"))
                    navController.context.startActivity(intent)
                }
                .padding(vertical = 8.dp)
        )
    }
}