package fr.infuseting.edt_app.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun DropdownMenu(title: String, items: List<String>, onItemClick: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val starColors = remember { mutableStateListOf(*Array(items.size) { Color.Black }) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = title,
                modifier = Modifier
                    .clickable { expanded = !expanded }
                    .padding(16.dp)
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = "★ $item",
                                color = starColors[index]
                            )
                        },
                        onClick = {
                            starColors[index] = if (starColors[index] == Color.Black) Color.Yellow else Color.Black
                            onItemClick(item)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}