package com.example.alakefak.data.repository

import com.example.alakefak.data.source.remote.network.APIModule.apiService

class RecipeRepository {

    suspend fun searchMealByName(name: String)=apiService.searchMealByName(name)

    suspend fun listMealsByFirstLetter(letter: Char)=apiService.listMealsByFirstLetter(letter)

    suspend fun listCategories()=apiService.listCategories()

    suspend fun listArea()=apiService.listArea()

    suspend fun listIngredients()=apiService.listIngredients()

    suspend fun filterByMAinIngredient(ingredient: String)=apiService.filterByMAinIngredient(ingredient)

    suspend fun filterByCategory(category: String)=apiService.filterByCategory(category)

    suspend fun filterByArea(area: String)=apiService.filterByArea(area)

    suspend fun lookupById(id: String)= apiService.lookupById(id)
}