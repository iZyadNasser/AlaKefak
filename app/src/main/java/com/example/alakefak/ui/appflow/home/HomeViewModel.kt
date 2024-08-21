package com.example.alakefak.ui.appflow.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.FavoriteRepository
import com.example.alakefak.data.repository.RecipeRepository
import com.example.alakefak.data.source.local.database.FavoritesDatabaseDao
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.data.source.remote.model.Meal
import kotlinx.coroutines.launch

class HomeViewModel(private val dao : FavoritesDatabaseDao):ViewModel() {

//    private val repo = FavoriteRepository(dao)
//    private var _favorite = MutableLiveData<MutableList<FavoritesInfo>>()
//    val favorite: LiveData<MutableList<FavoritesInfo>>
//        get() = _favorite


    private val repository = RecipeRepository()

    private var _recipes = MutableLiveData<List<Meal>>()
    val recipes: LiveData<List<Meal>>
        get() = _recipes

    init {
        getAllRecipesFromAPI()
    }

    private fun getAllRecipesFromAPI() {
        viewModelScope.launch {
            for (c in 'a'..'z') {
                val newRecipes = mutableListOf<Meal>()
                val response = repository.listMealsByFirstLetter(c).meals
                if (_recipes.value != null) {
                    for (item in _recipes.value!!) {
                        newRecipes.add(item)
                    }
                }

                if (response != null) {
                    for (item in response) {
                        if (item != null) {
                            newRecipes.add(item)
                        }
                    }
                }

                _recipes.value = newRecipes.toList()
            }
        }
    }

}