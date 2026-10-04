package com.arsenalsimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arsenalsimulator.ui.navigation.AppNavHost
import com.arsenalsimulator.ui.theme.ArsenalTheme
import com.arsenalsimulator.viewmodel.SettingsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val app = application as ArsenalApp
        setContent {
            val settingsVm: SettingsViewModel = viewModel(
                factory = SettingsViewModel.factory(app)
            )
            val dark by settingsVm.darkMode.collectAsStateWithLifecycle(initialValue = false)
            ArsenalTheme(darkTheme = dark) {
                AppNavHost(repository = app.repository, settingsViewModel = settingsVm)
            }
        }
    }
}
