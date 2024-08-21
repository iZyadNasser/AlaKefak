package com.example.alakefak.ui.appflow.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao


class FavoritesFragmentViewModelFactory(
    private val favoritesDatabaseDao: FavoritesDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesFragmentViewModel::class.java)) {
            return FavoritesFragmentViewModel(favoritesDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
