package com.example.alakefak.data.repository

import com.example.alakefak.data.source.remote.network.APIModule

class RecipeReopsitory {
    val apiServise= APIModule.apiServise


    suspend fun searchMealByName(name: String)=apiServise.searchMealByName(name)

    suspend fun listMealsByFirstLetter(letter: Char)=apiServise.listMealsByFirstLetter(letter)

    suspend fun listCategories()=apiServise.listCategories()


    suspend fun listArea()=apiServise.listArea()


    suspend fun listIngredients()=apiServise.listIngredients()


    suspend fun filterByMAinIngredient(ingredient: String)=apiServise.filterByMAinIngredient(ingredient)

    suspend fun filterByCategory(category: String)=apiServise.filterByCategory(category)

    suspend fun filterByArea(area: String)= apiServise.filterByArea(area)

}