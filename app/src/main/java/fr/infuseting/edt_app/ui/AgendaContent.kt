package fr.infuseting.edt_app.ui

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.JsonElement
import fr.infuseting.edt_app.util.RequestType
import fr.infuseting.edt_app.util.chooserJson

@Composable
fun AgendaContent(navController: NavController, context: Context, paddingValues: PaddingValues, adeBase : Int, adeRessources : Int) {
    Column(modifier = Modifier.padding(paddingValues)) {
        Text(
            text = "adeBase : ${adeBase.toString()}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Text(
            text = "adeRessources : ${adeRessources.toString()}",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

    }
}