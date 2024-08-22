package com.example.alakefak.ui.appflow.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val dao : FavoritesDatabaseDao):ViewModel() {
    private val repo = FavoriteRepository(dao)
    private var _favorite = MutableLiveData<List<FavoritesInfo>>()
    val favorite: LiveData<List<FavoritesInfo>>
        get() = _favorite

    fun getAllItems(){
        viewModelScope.launch {
            _favorite.value = repo.getAllFavorites()
        }
    }

}