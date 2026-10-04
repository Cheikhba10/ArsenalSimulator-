package com.arsenalsimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsenalsimulator.data.model.Weapon
import com.arsenalsimulator.data.repository.WeaponRepository
import com.arsenalsimulator.ui.components.WeaponSilhouetteView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulationScreen(nav: NavController, repo: WeaponRepository, id: Int) {
    var weapon by remember { mutableStateOf<Weapon?>(null) }
    var ammo by remember { mutableIntStateOf(30) }
    var shots by remember { mutableIntStateOf(0) }

    LaunchedEffect(id) { weapon = repo.byId(id) }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text(weapon?.name ?: "Simulation") },
            navigationIcon = {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }
        )
    }) { p ->
        Column(Modifier.padding(p).fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxWidth()) {
                WeaponSilhouetteView(weapon = weapon, shootSignal = shots)
            }
            Surface(tonalElevation = 4.dp) {
                Column(Modifier.padding(16.dp)) {
                    weapon?.let {
                        Text(it.name, fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge)
                        Text("${it.caliber ?: "-"} - Chargeur: ${it.magazine ?: "-"}")
                        Spacer(Modifier.height(8.dp))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Munitions virtuelles : $ammo", modifier = Modifier.weight(1f))
                        Text("Tirs : $shots")
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(enabled = ammo > 0, onClick = { ammo--; shots++ }) {
                            Text("Tirer (fictif)")
                        }
                        OutlinedButton(onClick = { ammo = 30; shots = 0 }) {
                            Text("Recharger")
                        }
                    }
                }
            }
        }
    }
}
