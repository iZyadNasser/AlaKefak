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
    private val repository = RecipeRepository()

    private var _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories


    var recipes = mutableListOf<Meal>()

    private var _notifyDataChange = MutableLiveData(false)
    val notifyDataChange: LiveData<Boolean>
        get() = _notifyDataChange



    init {
        getAllRecipesFromAPI()
        getCategories()
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
        for (item in recipes) {
            newRecipes.add(item)
        }
        viewModelScope.launch {
            for (c in 'a'..'z') {
                val response = repository.listMealsByFirstLetter(c).meals
                if (response != null) {
                    for (item in response) {
                        /**
                         * if (item in favoritesLsit) {
                         *  item.isFavorite = true
                         * }
                         */

                    }
                    for (item in response) {
                        if (item != null) {
                            recipes.add(item)
                        }
                    }
                }
            }
            _notifyDataChange.value = !_notifyDataChange.value!!
        }
    }

    fun getFilteredItems() {
        recipes = ArrayList()
        if (selectedFilter == NO_FILTER) {
            getAllRecipesFromAPI()
        } else {
            viewModelScope.launch {
                val reducedMeals = repository.filterByCategory(selectedFilter).meals

                if (reducedMeals != null) {
                    for (meal in reducedMeals) {
                        recipes.add(repository.lookupById(meal?.idMeal!!).meals?.get(0)!!)
                    }
                }

                _notifyDataChange.value = !_notifyDataChange.value!!
            }
        }
    }

    companion object {
        const val NO_FILTER = "all"
    }

}