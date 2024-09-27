package fr.infuseting.edt_app.ui


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import fr.infuseting.edt_app.util.colorChooser

@Composable
fun EDTContent(items: Map<String, String>, onItemClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorChooser(items))
            .padding(16.dp),

        contentAlignment = Alignment.Center

    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = items["summary"] ?: "",
                color = Color.White
            )
            Text(
                text = items["description"] ?: "",
                color = Color.White
            )
            Text(
                text = items["horaire"] ?: "",
                color = Color.White
            )
            Text(
                text = items["location"] ?: "",
                color = Color.White
            )

        }
    }
}