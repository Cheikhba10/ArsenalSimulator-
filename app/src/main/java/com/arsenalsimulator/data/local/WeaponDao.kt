package com.arsenalsimulator.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WeaponDao {
    @Query("SELECT * FROM weapons ORDER BY name")
    fun getAll(): Flow<List<WeaponEntity>>

    @Query("SELECT * FROM weapons WHERE category = :cat ORDER BY name")
    fun byCategory(cat: String): Flow<List<WeaponEntity>>

    @Query("SELECT * FROM weapons WHERE isFavorite = 1 ORDER BY name")
    fun favorites(): Flow<List<WeaponEntity>>

    @Query("SELECT * FROM weapons WHERE name LIKE '%' || :q || '%'")
    fun search(q: String): Flow<List<WeaponEntity>>

    @Query("SELECT * FROM weapons WHERE id = :id")
    suspend fun byId(id: Int): WeaponEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<WeaponEntity>)

    @Query("UPDATE weapons SET isFavorite = :fav WHERE id = :id")
    suspend fun setFavorite(id: Int, fav: Boolean)

    @Query("SELECT COUNT(*) FROM weapons")
    suspend fun count(): Int
}
