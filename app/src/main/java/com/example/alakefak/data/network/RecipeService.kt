package com.example.alakefak.data.network

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

interface RecipeService {


    @GET(SEARCH_MEAL_BY_NAME_ENDPOINT)
    suspend fun searchMealByName(): ArrayList<SearchMealByNameResponse>

    @GET(LIST_MEALS_BY_FIRST_LETTER_ENDPOINT)
    suspend fun listMealsByFirstLetter(): ArrayList<ListMealsByFirstLetterResponse>

    @GET(LIST_CATEGORIES_ENDPOINT)
    suspend fun listCategories(): ArrayList<ListAllCategoriesResponse>


    @GET(LIST_AREA_ENDPOINT)
    suspend fun listArea(): ArrayList<ListAllAreaResponse>


    @GET(LIST_INGREDIENTS_ENDPOINT)
    suspend fun listIngredients(): ArrayList<ListAllIngredientsResponse>


    @GET(FILTER_BY_MAIN_INGREDIENT)
    suspend fun filterByMAinIngredient(): ArrayList<FilterByMainIngredientResponse>

    @GET(FILTER_BY_CATEGORY)
    suspend fun filterByCategory(): ArrayList<FilterByCategoryResponse>

    @GET(FILTER_BY_AREA)
    suspend fun filterByArea(): ArrayList<FilterByAreaResponse>
}