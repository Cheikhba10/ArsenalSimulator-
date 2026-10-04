package com.arsenalsimulator.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.arsenalsimulator.data.repository.WeaponRepository
import com.arsenalsimulator.ui.screens.*
import com.arsenalsimulator.viewmodel.SettingsViewModel

@Composable
fun AppNavHost(repository: WeaponRepository, settingsViewModel: SettingsViewModel) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav) }
        composable("arsenal") { ArsenalScreen(nav, repository) }
        composable("categories") { CategoriesScreen(nav) }
        composable("compare") { CompareScreen(nav, repository) }
        composable("info") { InfoScreen(nav, settingsViewModel) }
        composable("simulation/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("id") ?: 0
            SimulationScreen(nav, repository, id)
        }
        composable("detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { back ->
            val id = back.arguments?.getInt("id") ?: 0
            WeaponDetailScreen(nav, repository, id)
        }
    }
}
