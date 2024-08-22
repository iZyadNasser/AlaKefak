package com.example.alakefak.data.repository

import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo

class FavoriteRepository (private val dao : FavoritesDatabaseDao){
    suspend fun insertFavorite(favoritesInfo: FavoritesInfo){
        dao.insertFavorite(favoritesInfo)
    }
    suspend fun deleteFavorite(favoritesInfo: FavoritesInfo){
        dao.deleteFavorite(favoritesInfo)
    }
    suspend fun getAllFavorites(userId: Long) : List<FavoritesInfo>{
        return dao.getAllFavorites(userId)
    }
    suspend fun findItem(id : String, userId: Long): FavoritesInfo?{
        return dao.findItem(id, userId)
    }

    suspend fun getNumOfFavorites(userId: Long): Long {
        return dao.getNumOfFavorites(userId)
    }
}