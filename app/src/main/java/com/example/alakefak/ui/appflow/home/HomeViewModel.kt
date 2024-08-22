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
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

    var clickState = false

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
        val newRecipes = mutableListOf<Meal>()
        if (_recipes.value != null) {
            for (item in _recipes.value!!) {
                newRecipes.add(item)
            }
        }
        viewModelScope.launch {
            for (c in 'a'..'z') {
                val response = repository.listMealsByFirstLetter(c).meals
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
                clickState = true
                val reducedMeals = viewModelScope.async {
                    repository.filterByCategory(selectedFilter).meals as List<Meal>?
                }

                val deferredMeals = reducedMeals.await()?.map { meal ->
                    viewModelScope.async {
                        repository.lookupById(meal.idMeal!!)
                    }
                }

                _recipes.value = deferredMeals?.awaitAll()?.filterNotNull()?.map { it.meals?.get(0)!! } ?: emptyList()
                clickState = false
            }
        }
    }

    companion object {
        const val NO_FILTER = "all"
    }

}