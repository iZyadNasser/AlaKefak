package com.example.alakefak.ui.appflow.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import kotlinx.coroutines.launch

class HomeViewModel(private val dao : FavoritesDatabaseDao):ViewModel() {

    private val repo = FavoriteRepository(dao)
    private var _favorite = MutableLiveData<List<FavoritesInfo>>()
    val favorite: LiveData<List<FavoritesInfo>>
        get() = _favorite

    fun insertFavorite(){
        viewModelScope.launch {

        }
    }

    fun deleteFavorite(){
        viewModelScope.launch {

        }
    }


}