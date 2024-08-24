package com.example.alakefak.ui.appflow.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao

class DetailsViewModelFactory(
    private val favoritesDatabaseDao: FavoritesDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailsViewModel::class.java)) {
            return DetailsViewModel(favoritesDatabaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}