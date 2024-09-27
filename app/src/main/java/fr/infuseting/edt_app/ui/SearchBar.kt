package fr.infuseting.edt_app.ui


import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text

@Composable
fun SearchBar(searchQuery: MutableState<String>) {
    TextField(
        value = searchQuery.value,
        onValueChange = { searchQuery.value = it },
        placeholder = { Text("Rechercher") },
        modifier = Modifier.fillMaxWidth()
    )
}