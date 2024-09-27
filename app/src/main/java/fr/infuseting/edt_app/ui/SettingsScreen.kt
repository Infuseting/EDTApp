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
                IconButton(onClick = { navController.navigate("home") }) {
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
    }
}