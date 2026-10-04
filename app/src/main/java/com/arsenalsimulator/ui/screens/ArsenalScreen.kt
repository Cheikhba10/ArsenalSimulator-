package com.arsenalsimulator.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.arsenalsimulator.data.model.Weapon
import com.arsenalsimulator.data.repository.WeaponRepository
import com.arsenalsimulator.viewmodel.ArsenalViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArsenalScreen(nav: NavController, repo: WeaponRepository) {
    val vm: ArsenalViewModel = viewModel(factory = ArsenalViewModel.factory(repo))
    val list by vm.weapons.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Arsenal") },
            navigationIcon = {
                IconButton(onClick = { nav.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, null)
                }
            },
            actions = {
                IconButton(onClick = { vm.toggleFavorites() }) {
                    Icon(Icons.Default.FavoriteBorder, null)
                }
            }
        )
    }) { p ->
        Column(Modifier.padding(p)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it; vm.setQuery(it) },
                placeholder = { Text("Rechercher une arme...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                singleLine = true
            )
            LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                items(list, key = { it.id }) { w ->
                    WeaponRow(w,
                        onClick = { nav.navigate("detail/${w.id}") },
                        onFav = { vm.toggleFavorite(w) })
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun WeaponRow(w: Weapon, onClick: () -> Unit, onFav: () -> Unit) {
    Row(Modifier.fillMaxWidth().clickable(onClick = onClick).padding(16.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(w.name, fontWeight = FontWeight.SemiBold)
            Text("${w.type} - ${w.country} - ${w.era}", style = MaterialTheme.typography.bodySmall)
        }
        IconButton(onClick = onFav) {
            Icon(if (w.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null)
        }
    }
}
