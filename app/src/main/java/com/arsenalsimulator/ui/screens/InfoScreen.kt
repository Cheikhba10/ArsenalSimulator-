package com.arsenalsimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsenalsimulator.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoScreen(nav: NavController, vm: SettingsViewModel) {
    val dark by vm.darkMode.collectAsState(initial = false)
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Informations") },
            navigationIcon = {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }
        )
    }) { p ->
        Column(Modifier.padding(p).padding(16.dp)) {
            Text("Arsenal Simulator", style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text("Version 1.0.0", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            Text("Cette application est un simulateur video-ludique a but educatif et de divertissement. Elle presente des armes historiques et modernes dans un environnement virtuel. Aucune instruction de fabrication ou d'utilisation reelle n'est fournie.",
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(24.dp))
            Row {
                Text("Mode sombre", Modifier.weight(1f))
                Switch(checked = dark, onCheckedChange = { vm.setDark(it) })
            }
        }
    }
}
