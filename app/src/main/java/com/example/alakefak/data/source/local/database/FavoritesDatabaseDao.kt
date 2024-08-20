package com.example.alakefak.data.source.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.alakefak.data.source.local.model.FavoritesInfo

@Dao
interface FavoritesDatabaseDao {
    @Query("SELECT * FROM favoritesInfo ")
    suspend fun getAllFavorites(): List<FavoritesInfo>

    @Query("SELECT * FROM favoritesInfo WHERE id = :id")
    suspend fun findItem(id : String): FavoritesInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favoritesInfo: FavoritesInfo)

    @Delete
    suspend fun deleteFavorite(favoritesInfo: FavoritesInfo)

    @Query("SELECT COUNT(*) FROM favoritesInfo")
    suspend fun getNumOfFavorites(): Long
}