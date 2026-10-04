package com.arsenalsimulator.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weapons")
data class WeaponEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val category: String,
    val era: String,
    val country: String,
    val type: String,
    val caliber: String?,
    val magazine: String?,
    val range: String?,
    val weightKg: Float?,
    val description: String,
    val modelAsset: String?,
    val isFavorite: Boolean = false
)
