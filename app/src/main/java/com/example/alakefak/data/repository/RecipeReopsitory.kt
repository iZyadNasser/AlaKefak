package com.example.alakefak.data.repository

import com.example.alakefak.data.network.APIModule
import com.example.alakefak.data.network.APIModule.FILTER_BY_AREA
import com.example.alakefak.data.network.APIModule.FILTER_BY_CATEGORY
import com.example.alakefak.data.network.APIModule.FILTER_BY_MAIN_INGREDIENT
import com.example.alakefak.data.network.APIModule.LIST_AREA_ENDPOINT
import com.example.alakefak.data.network.APIModule.LIST_CATEGORIES_ENDPOINT
import com.example.alakefak.data.network.APIModule.LIST_INGREDIENTS_ENDPOINT
import com.example.alakefak.data.network.APIModule.LIST_MEALS_BY_FIRST_LETTER_ENDPOINT
import com.example.alakefak.data.network.APIModule.SEARCH_MEAL_BY_NAME_ENDPOINT
import com.example.alakefak.data.source.local.model.FilterByAreaResponse
import com.example.alakefak.data.source.local.model.FilterByCategoryResponse
import com.example.alakefak.data.source.local.model.FilterByMainIngredientResponse
import com.example.alakefak.data.source.local.model.ListAllAreaResponse
import com.example.alakefak.data.source.local.model.ListAllCategoriesResponse
import com.example.alakefak.data.source.local.model.ListAllIngredientsResponse
import com.example.alakefak.data.source.local.model.ListMealsByFirstLetterResponse
import com.example.alakefak.data.source.local.model.SearchMealByNameResponse
import retrofit2.http.GET

class RecipeReopsitory {
    val apiServise= APIModule.apiServise


    suspend fun searchMealByName()=apiServise.searchMealByName()

    suspend fun listMealsByFirstLetter()=apiServise.listMealsByFirstLetter()

    suspend fun listCategories()=apiServise.listCategories()


    suspend fun listArea()=apiServise.listArea()


    suspend fun listIngredients()=apiServise.listIngredients()


    suspend fun filterByMAinIngredient()=apiServise.filterByMAinIngredient()

    suspend fun filterByCategory()=apiServise.filterByCategory()

    suspend fun filterByArea()= apiServise.filterByArea()

}