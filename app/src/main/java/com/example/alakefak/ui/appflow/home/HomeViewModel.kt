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
import kotlinx.coroutines.launch

class HomeViewModel(dao: FavoritesDatabaseDao) : ViewModel() {
    var selectedFilter = NO_FILTER
    var recipes = mutableListOf<Meal>()


    private val repository = RecipeRepository()
    private val favRepo = FavoriteRepository(dao)

    private var _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    private var _notifyDataChange = MutableLiveData(false)
    val notifyDataChange: LiveData<Boolean>
        get() = _notifyDataChange

    private var _categoriesLoading = MutableLiveData(false)
    val categoriesLoading: LiveData<Boolean>
        get() = _categoriesLoading

    private var _recipesLoading = MutableLiveData(false)
    val recipesLoading: LiveData<Boolean>
        get() = _recipesLoading

    private var _favAdded = MutableLiveData<List<FavoritesInfo>>()
    val favAdded: LiveData<List<FavoritesInfo>>
        get() = _favAdded


    init {
        getCategories()
//        getAllRecipesFromAPI()
        getAllFavs()
    }

    private fun getCategories() {
        viewModelScope.launch {
            _categoriesLoading.value = true
            val meals = repository.listCategories().meals
            val listOfCategories = mutableListOf<String>()

            if (meals != null) {
                for (meal in meals) {
                    if (meal != null) {
                        listOfCategories.add(meal.strCategory!!)
                    }
                }
            }
            _categoriesLoading.value = false
            _categories.value = listOfCategories
        }
    }

    private fun getAllRecipesFromAPI() {
        viewModelScope.launch {
            currentlyLoading = true
            _recipesLoading.value = true
            for (item in recipes) {
                if (favRepo.findItem(item.idMeal!!, RecipeActivity.curUser?.id!!) != null) {
                    item.isFavorite = true
                }
                recipes.add(item)
            }
            for (c in 'a'..'z') {
                val response = repository.listMealsByFirstLetter(c).meals
                if (response != null) {
                    for (item in response) {
                        if (item != null) {
                            if (favRepo.findItem(
                                    item.idMeal!!,
                                    RecipeActivity.curUser?.id!!
                                ) != null
                            ) {
                                item.isFavorite = true
                            }
                            recipes.add(item)
                        }
                    }
                }
            }
            _recipesLoading.value = false
            currentlyLoading = false
            _notifyDataChange.value = !_notifyDataChange.value!!
        }
    }

    private fun getAllFavs() {
        viewModelScope.launch {
            _favAdded.value = favRepo.getAllFavorites(RecipeActivity.curUser?.id!!)
        }
    }

    fun getFilteredItems() {
        recipes = ArrayList()
        if (selectedFilter == NO_FILTER) {
            getAllRecipesFromAPI()
        } else {
            viewModelScope.launch {
                currentlyLoading = true
                _recipesLoading.value = true
                val reducedMeals = repository.filterByCategory(selectedFilter).meals

                if (reducedMeals != null) {
                    for (meal in reducedMeals) {
                        val item = repository.lookupById(meal?.idMeal!!).meals?.get(0)!!
                        if (favRepo.findItem(item.idMeal!!, RecipeActivity.curUser?.id!!) != null) {
                            item.isFavorite = true
                        }
                        recipes.add(item)
                    }
                }

                _recipesLoading.value = false
                currentlyLoading = false
                _notifyDataChange.value = !_notifyDataChange.value!!
            }
        }
    }

    fun deleteFav(meal: Meal) {
        viewModelScope.launch {
            meal.isFavorite = false
            favRepo.deleteFavorite(
                favRepo.findItem(
                    meal.idMeal ?: "",
                    RecipeActivity.curUser?.id!!
                )!!
            )
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

    private fun resetApi() {
        recipes = ArrayList()
    }

    fun getNewFavs() {
        if (selectedFilter == NO_FILTER) {
            resetApi()
            getAllRecipesFromAPI()
        } else {
            resetApi()
            getFilteredItems()
        }
    }

    companion object {
        const val NO_FILTER = "all"
        var currentlyLoading = false
    }

}