package com.arsenalsimulator.data.model

enum class Category(val label: String) {
    PISTOL("Pistolets"),
    REVOLVER("Revolvers"),
    SMG("Pistolets-mitrailleurs"),
    RIFLE("Fusils"),
    ASSAULT_RIFLE("Fusils d'assaut"),
    SNIPER("Fusils de precision"),
    MACHINE_GUN("Mitrailleuses"),
    SHOTGUN("Fusils de chasse"),
    HISTORICAL("Armes historiques"),
    ARTILLERY("Artillerie"),
    EQUIPMENT("Autres equipements")
}

data class Weapon(
    val id: Int = 0,
    val name: String,
    val category: Category,
    val era: String,
    val country: String,
    val type: String,
    val caliber: String? = null,
    val magazine: String? = null,
    val range: String? = null,
    val weightKg: Float? = null,
    val description: String,
    val modelAsset: String? = null,
    val isFavorite: Boolean = false
)
