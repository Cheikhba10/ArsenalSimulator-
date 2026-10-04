package com.arsenalsimulator

import android.app.Application
import com.arsenalsimulator.data.local.AppDatabase
import com.arsenalsimulator.data.repository.WeaponRepository
import com.arsenalsimulator.data.seed.WeaponSeedData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArsenalApp : Application() {
    val database by lazy { AppDatabase.get(this) }
    val repository by lazy { WeaponRepository(database.weaponDao()) }

    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            if (repository.count() == 0) repository.seed(WeaponSeedData.items())
        }
    }
}
