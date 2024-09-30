package fr.infuseting.edt_app.ui


import androidx.compose.foundation.layout.Box
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(searchQuery: MutableState<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        TextField(
            value = searchQuery.value,
            onValueChange = {
                searchQuery.value = it
            },
            placeholder = { Text("Rechercher") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}