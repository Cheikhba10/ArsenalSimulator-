package com.arsenalsimulator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arsenalsimulator.data.model.Weapon
import com.arsenalsimulator.data.repository.WeaponRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(nav: NavController, repo: WeaponRepository) {
    var all by remember { mutableStateOf<List<Weapon>>(emptyList()) }
    var a by remember { mutableStateOf<Weapon?>(null) }
    var b by remember { mutableStateOf<Weapon?>(null) }
    LaunchedEffect(Unit) { repo.all().collect { all = it } }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Comparer") },
            navigationIcon = {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            }
        )
    }) { p ->
        Column(Modifier.padding(p).padding(16.dp).verticalScroll(rememberScrollState())) {
            WeaponPicker("Arme 1", all, a) { a = it }
            Spacer(Modifier.height(8.dp))
            WeaponPicker("Arme 2", all, b) { b = it }
            Spacer(Modifier.height(16.dp))
            if (a != null && b != null) CompareTable(a!!, b!!)
        }
    }
}

@Composable
private fun CompareTable(a: Weapon, b: Weapon) {
    val rows = listOf(
        "Nom" to (a.name to b.name),
        "Calibre" to ((a.caliber ?: "-") to (b.caliber ?: "-")),
        "Capacite" to ((a.magazine ?: "-") to (b.magazine ?: "-")),
        "Portee" to ((a.range ?: "-") to (b.range ?: "-")),
        "Poids" to ((a.weightKg?.toString() ?: "-") to (b.weightKg?.toString() ?: "-")),
        "Type" to (a.type to b.type),
        "Epoque" to (a.era to b.era),
        "Pays" to (a.country to b.country)
    )
    rows.forEach { (k, pair) ->
        Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(k, Modifier.weight(1f), fontWeight = FontWeight.SemiBold)
            Text(pair.first, Modifier.weight(1f))
            Text(pair.second, Modifier.weight(1f))
        }
        HorizontalDivider()
    }
}

@Composable
private fun WeaponPicker(label: String, items: List<Weapon>, selected: Weapon?, onPick: (Weapon) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
            Text("$label : ${selected?.name ?: "Choisir"}")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            items.forEach { w ->
                DropdownMenuItem(text = { Text(w.name) }, onClick = { onPick(w); expanded = false })
            }
        }
    }
}
