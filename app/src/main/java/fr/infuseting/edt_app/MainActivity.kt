package fr.infuseting.edt_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import fr.infuseting.edt_app.ui.BottomBar
import fr.infuseting.edt_app.ui.MainContent
import fr.infuseting.edt_app.ui.SettingsScreen
import fr.infuseting.edt_app.ui.theme.EDTAppTheme
import fr.infuseting.edt_app.util.PreferencesManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = this
            val systemDarkMode = isSystemInDarkTheme()
            var darkModeEnabled by remember { mutableStateOf(PreferencesManager.isDarkModeEnabled(context) ?: systemDarkMode) }

            EDTAppTheme(darkTheme = darkModeEnabled) {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute != "settings") {
                            BottomBar(navController)
                        }
                    }
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = "mainContent") {
                        composable("mainContent") { MainContent(paddingValues) }
                        composable("settings") { SettingsScreen(navController, onDarkModeToggle = { darkModeEnabled = it }) }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    EDTAppTheme(darkTheme = false) {
        MainContent(PaddingValues(16.dp))
    }
}