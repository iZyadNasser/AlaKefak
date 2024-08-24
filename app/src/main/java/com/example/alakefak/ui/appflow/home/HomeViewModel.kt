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
import com.example.alakefak.ui.appflow.RecipeActivity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlin.time.measureTime

class HomeViewModel(private val dao : FavoritesDatabaseDao):ViewModel() {
    var selectedFilter = NO_FILTER

    private val repository = RecipeRepository()
    private val favRepo = FavoriteRepository(dao)

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
        viewModelScope.launch {
            for (item in recipes) {
                if (favRepo.findItem(item.idMeal!!, RecipeActivity.curUser?.id!!) != null) {
                    item.isFavorite = true
                }
                newRecipes.add(item)
            }
            for (c in 'a'..'z') {
                val response = repository.listMealsByFirstLetter(c).meals
                if (response != null) {
                    for (item in response) {
                        if (item != null) {
                            if (favRepo.findItem(item.idMeal!!, RecipeActivity.curUser?.id!!) != null) {
                                item.isFavorite = true
                            }
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
                        val item = repository.lookupById(meal?.idMeal!!).meals?.get(0)!!
                        if (favRepo.findItem(item?.idMeal!!, RecipeActivity.curUser?.id!!) != null) {
                            item.isFavorite = true
                        }
                        recipes.add(item)
                    }
                }

                _notifyDataChange.value = !_notifyDataChange.value!!
            }
        }
    }

    fun deleteFav(meal: Meal) {
        viewModelScope.launch {
            meal.isFavorite = false
            favRepo.deleteFavorite(favRepo.findItem(meal.idMeal ?: "",RecipeActivity.curUser?.id!!)!!)
        }
    }

    fun insertFav(item: Meal) {
        val newFav = FavoritesInfo(
            id = item.idMeal ?: "",
            recipeName = item.strMeal ?: "",
            recipeCategory = item.strCategory ?: "",
            recipeImg = item.strMealThumb ?: "",
            recipeArea = item.strArea ?: "",
            userId = RecipeActivity.curUser?.id!!
        )
        viewModelScope.launch {
            item.isFavorite = true
            favRepo.insertFavorite(newFav)
        }
    }

    companion object {
        const val NO_FILTER = "all"
    }

}