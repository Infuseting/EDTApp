package fr.infuseting.edt_app.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import fr.infuseting.edt_app.R

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar {
        IconButton(onClick = { navController.navigate("settings") }) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_settings),
                contentDescription = "Settings"
            )
        }
        Spacer(modifier = Modifier.weight(0.75f))

        Spacer(modifier = Modifier.weight(1f))
    }
}