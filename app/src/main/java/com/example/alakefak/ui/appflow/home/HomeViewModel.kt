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
    var selectedFilter = NO_FILTER

    private var _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories


    private val repository = RecipeRepository()

    private var _recipes = MutableLiveData<List<Meal>>()
    val recipes: LiveData<List<Meal>>
        get() = _recipes

    init {
        getCategories()
        getAllRecipesFromAPI()
    }

    private fun getCategories() {
        viewModelScope.launch {
            val meals = repository.listCategories().meals
            val listOfCategories = mutableListOf<String>()

            if (meals != null) {
                for (meal in meals) {
                    if (meal != null) {
                        listOfCategories.add(meal.strCategory!!)
                    }
                }
            }

            _categories.value = listOfCategories
        }
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

    fun getFilteredItems() {
        if (selectedFilter == NO_FILTER) {
            _recipes.value = listOf()
            getAllRecipesFromAPI()
        } else {
            viewModelScope.launch {
                _recipes.value = repository.filterByCategory(selectedFilter).meals as List<Meal>?
            }
        }
    }

    companion object {
        const val NO_FILTER = "all"
    }

}