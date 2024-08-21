package com.example.alakefak.ui.appflow.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.RecipeRepository
import com.example.alakefak.data.source.local.model.SearchResult
import com.example.alakefak.data.source.remote.model.Meal
import kotlinx.coroutines.launch

class SearchFragmentViewModel : ViewModel() {
    private val repository = RecipeRepository()
    private var _searchResults = MutableLiveData<Set<SearchResult>>()
    val searchResults: LiveData<Set<SearchResult>>
        get() = _searchResults

    fun search(text: String) {
        resetSearch()
        if (text != "") {
            viewModelScope.launch {
                addMatchedName(text)
                addMealsOfCategory(text)
                addMealsOfArea(text)
                addMealsOfIngredient(text)
            }
        }
    }

    private fun resetSearch() {
        _searchResults.value = setOf()
    }

    private suspend fun addMatchedName(name: String) {
        val response = repository.searchMealByName(name).meals
        val newResult = mutableSetOf<SearchResult>()
        if (_searchResults.value != null) {
            for (item in _searchResults.value!!) {
                newResult.add(item)
            }
        }
        if (response != null) {
            for (meal in response) {
                if (meal != null) {
                    val newMeal = convertMealToSearchResult(meal)
                    newResult.add(newMeal)
                }
            }

        }

        _searchResults.value = newResult.toSet()
    }

    private suspend fun addMealsOfCategory(category: String) {
        val newResult = mutableSetOf<SearchResult>()
        val initialResponse = repository.filterByCategory(category).meals
        for (i in 0..(initialResponse?.size?.minus(1) ?: -1)) {
            val response = repository.lookupById(initialResponse?.get(i)?.idMeal ?: "").meals
            if (_searchResults.value != null) {
                for (item in _searchResults.value!!) {
                    newResult.add(item)
                }
            }
            if (response != null) {
                for (meal in response) {
                    if (meal != null) {
                        val newMeal = convertMealToSearchResult(meal)
                        newResult.add(newMeal)
                    }
                }
            }
            _searchResults.value = newResult.toSet()
        }
    }

    private suspend fun addMealsOfArea(area: String) {
        val newResult = mutableSetOf<SearchResult>()
        val initialResponse = repository.filterByArea(area).meals
        for (i in 0..(initialResponse?.size?.minus(1) ?: -1)) {
            val response = repository.lookupById(initialResponse?.get(i)?.idMeal ?: "").meals
            if (_searchResults.value != null) {
                for (item in _searchResults.value!!) {
                    newResult.add(item)
                }
            }
            if (response != null) {
                for (meal in response) {
                    if (meal != null) {
                        val newMeal = convertMealToSearchResult(meal)
                        newResult.add(newMeal)
                    }
                }
            }
            _searchResults.value = newResult.toSet()
        }
    }

    private suspend fun addMealsOfIngredient(ingredient: String) {
        val newResult = mutableSetOf<SearchResult>()
        val initialResponse = repository.filterByMAinIngredient(ingredient).meals
        for (i in 0..(initialResponse?.size?.minus(1) ?: -1)) {
            val response = repository.lookupById(initialResponse?.get(i)?.idMeal ?: "").meals
            if (_searchResults.value != null) {
                for (item in _searchResults.value!!) {
                    newResult.add(item)
                }
            }
            if (response != null) {
                for (meal in response) {
                    if (meal != null) {
                        _searchResults.value = _searchResults.value?.plus(
                            listOf(
                                convertMealToSearchResult(meal)
                            )
                        )
                    }
                }
            }
            _searchResults.value = newResult.toSet()
        }
    }

    private fun convertMealToSearchResult(meal: Meal): SearchResult {
        val result = SearchResult(
            id = meal.idMeal ?: "",
            name = meal.strMeal ?: "",
            imageUrl = meal.strMealThumb ?: "",
            category = meal.strCategory ?: "",
            area = meal.strArea ?: ""
        )

        return result
    }
}