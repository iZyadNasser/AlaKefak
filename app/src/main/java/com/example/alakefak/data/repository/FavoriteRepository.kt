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
    suspend fun getAllFavorites() : List<FavoritesInfo>{
        return dao.getAllFavorites()
    }
    suspend fun findItem(id : String): FavoritesInfo?{
        return dao.findItem(id)
    }
}