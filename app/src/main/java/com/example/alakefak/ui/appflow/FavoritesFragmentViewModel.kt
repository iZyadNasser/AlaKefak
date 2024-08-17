package com.example.alakefak.ui.appflow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.model.FavoritesInfo
import kotlinx.coroutines.launch

class FavoritesFragmentViewModel(private val repo : FavoriteRepository):ViewModel() {
    var favorite = MutableLiveData<List<FavoritesInfo>>()

    fun getAllItems(){
        viewModelScope.launch {
            favorite.value = repo.getAllFavorites()
        }
    }
    fun insertNewItem(favoriteItem: FavoritesInfo){
        viewModelScope.launch {
            repo.insertFavorite(favoriteItem)
        }
    }
    fun deleteItem(deletedItem : FavoritesInfo){
        viewModelScope.launch {
            repo.deleteFavorite(deletedItem)
        }
    }

}