package fr.infuseting.edt_app.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import fr.infuseting.edt_app.util.RequestType
import fr.infuseting.edt_app.util.chooserJson
import fr.infuseting.edt_app.util.filterItems
import kotlinx.serialization.json.Json

@Composable
fun MainContent(paddingValues: PaddingValues) {
    val searchQuery = remember { mutableStateOf("") }

    val salleItems = (chooserJson(RequestType.SALLE))
    val profItems = (chooserJson(RequestType.PROF))
    val univItems = (chooserJson(RequestType.UNIV))

    val filteredSalleItems = filterItems(searchQuery.value, salleItems)
    val filteredProfItems = filterItems(searchQuery.value, profItems)
    val filteredUnivItems = filterItems(searchQuery.value, univItems)

    Column(modifier = Modifier.padding(paddingValues)) {
        Text(
            text = "DESIGN EN COURS DE CREATION ! CECI EST UNE VERSION BETA !",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "ATTENTION LES COURS SUPPLEMENTAIRE NE SONT PAS NECESSAIREMENT ECRIT SUR L'EMPLOI DU TEMPS",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(searchQuery)
        Spacer(modifier = Modifier.height(16.dp))
        Log.d("MainContent", "SalleItems: $filteredSalleItems");
        //DropdownMenu(title = "Salle", items = filteredSalleItems) { item ->
            // Handle item click
        //}
        //DropdownMenu(title = "Professeur", items = filteredProfItems) { item ->
            // Handle item click
        //}
        //DropdownMenu(title = "Université", items = filteredUnivItems) { item ->
            // Handle item click
        //}
    }
}