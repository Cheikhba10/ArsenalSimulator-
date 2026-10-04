package com.arsenalsimulator.data.repository

import com.arsenalsimulator.data.local.WeaponDao
import com.arsenalsimulator.data.local.WeaponEntity
import com.arsenalsimulator.data.model.Category
import com.arsenalsimulator.data.model.Weapon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WeaponRepository(private val dao: WeaponDao) {

    fun all(): Flow<List<Weapon>> = dao.getAll().map { it.map(::toModel) }
    fun favorites(): Flow<List<Weapon>> = dao.favorites().map { it.map(::toModel) }
    fun byCategory(cat: Category): Flow<List<Weapon>> =
        dao.byCategory(cat.name).map { it.map(::toModel) }
    fun search(q: String): Flow<List<Weapon>> =
        dao.search(q).map { it.map(::toModel) }

    suspend fun byId(id: Int): Weapon? = dao.byId(id)?.let(::toModel)
    suspend fun toggleFavorite(w: Weapon) = dao.setFavorite(w.id, !w.isFavorite)
    suspend fun count() = dao.count()
    suspend fun seed(items: List<WeaponEntity>) = dao.insertAll(items)

    private fun toModel(e: WeaponEntity) = Weapon(
        id = e.id,
        name = e.name,
        category = Category.valueOf(e.category),
        era = e.era,
        country = e.country,
        type = e.type,
        caliber = e.caliber,
        magazine = e.magazine,
        range = e.range,
        weightKg = e.weightKg,
        description = e.description,
        modelAsset = e.modelAsset,
        isFavorite = e.isFavorite
    )
}
