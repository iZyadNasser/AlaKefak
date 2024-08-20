package com.example.alakefak.ui.appflow.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.ui.appflow.favorites.FavoritesFragmentViewModel


class ProfileViewModelFactory(
    private val favoritesDatabaseDao: FavoritesDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(favoritesDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
