package com.example.alakefak.ui.appflow.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.ui.appflow.RecipeActivity
import kotlinx.coroutines.launch

class ProfileViewModel(private val favoritesDatabaseDao: FavoritesDatabaseDao) : ViewModel() {
    private val repository = FavoriteRepository(favoritesDatabaseDao)

    private var _numOfFavorites = MutableLiveData<Long>()
    val numOfFavorites: LiveData<Long>
        get() = _numOfFavorites

    private var _favoriteCategory = MutableLiveData<String>()
    val favoriteCategory: LiveData<String>
        get() = _favoriteCategory

    private var _favoriteArea = MutableLiveData<String>()
    val favoriteArea: LiveData<String>
        get() = _favoriteArea

    private var _allFavorites = MutableLiveData<List<FavoritesInfo>>()
    val allFavorites: LiveData<List<FavoritesInfo>>
        get() = _allFavorites

    private val categories = mutableMapOf<String, Long>()
    private val areas = mutableMapOf<String, Long>()

    init {
        viewModelScope.launch {
            _numOfFavorites.value = repository.getNumOfFavorites(RecipeActivity.curUser?.id!!)
        }
        viewModelScope.launch {
            getAllFavorites()
        }
    }

    private suspend fun getAllFavorites() {
        _allFavorites.value = repository.getAllFavorites(RecipeActivity.curUser?.id!!)
    }

    fun calculateFavorites() {
        calculateFavoriteCategory()
        calculateFavoriteArea()
    }

    private fun calculateFavoriteCategory() {
        viewModelScope.launch {
            categories.clear()
            for (fav in _allFavorites.value?:ArrayList()) {
                if (categories.containsKey(fav.recipeCategory) && categories[fav.recipeCategory] != null) {
                    categories[fav.recipeCategory] = categories[fav.recipeCategory]!! + 1L
                } else {
                    categories[fav.recipeCategory] = 1L
                }
            }

            _favoriteCategory.value = categories.maxByOrNull { it.value }?.key
        }
    }

    private fun calculateFavoriteArea() {
        viewModelScope.launch {
            areas.clear()
            for (fav in _allFavorites.value?:ArrayList()) {
                if (areas.containsKey(fav.recipeArea) && areas[fav.recipeArea] != null) {
                    areas[fav.recipeArea] = areas[fav.recipeArea]!! + 1L
                } else {
                    areas[fav.recipeArea] = 1L
                }
            }

            _favoriteArea.value = areas.maxByOrNull { it.value }?.key
        }
    }
}