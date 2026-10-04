package com.arsenalsimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsenalsimulator.data.model.Weapon
import com.arsenalsimulator.data.repository.WeaponRepository
import com.arsenalsimulator.ui.components.WeaponSilhouetteView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponDetailScreen(nav: NavController, repo: WeaponRepository, id: Int) {
    var w by remember { mutableStateOf<Weapon?>(null) }
    LaunchedEffect(id) { w = repo.byId(id) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(w?.name ?: "Detail") },
            navigationIcon = {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
            actions = {
                IconButton(onClick = { nav.navigate("simulation/$id") }) {
                    Icon(Icons.Default.PlayArrow, null)
                }
            }
        )
    }) { p ->
        w?.let { weapon ->
            Column(Modifier.padding(p).padding(16.dp).verticalScroll(rememberScrollState())) {
                Box(Modifier.fillMaxWidth().height(220.dp)) {
                    WeaponSilhouetteView(weapon = weapon)
                }
                Spacer(Modifier.height(16.dp))
                listOf(
                    "Type" to weapon.type,
                    "Epoque" to weapon.era,
                    "Pays" to weapon.country,
                    "Calibre" to (weapon.caliber ?: "-"),
                    "Chargeur" to (weapon.magazine ?: "-"),
                    "Portee" to (weapon.range ?: "-"),
                    "Poids" to (weapon.weightKg?.let { "$it kg" } ?: "-")
                ).forEach { (k, v) ->
                    Row(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Text(k, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
                        Text(v, Modifier.weight(2f))
                    }
                    HorizontalDivider()
                }
                Spacer(Modifier.height(16.dp))
                Text("Description", fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(weapon.description, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}
