package com.example.alakefak.data.source.remote.network

import com.example.alakefak.data.source.remote.model.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

const val SEARCH_MEAL_BY_NAME_ENDPOINT = "search.php"
const val LIST_MEALS_BY_FIRST_LETTER_ENDPOINT = "search.php"
const val LIST_CATEGORIES_ENDPOINT = "list.php?c=list"
const val LIST_AREA_ENDPOINT = "list.php?a=list"
const val LIST_INGREDIENTS_ENDPOINT = "list.php?i=list"
const val FILTER_BY_MAIN_INGREDIENT = "filter.php"
const val FILTER_BY_CATEGORY = "filter.php"
const val FILTER_BY_AREA = "filter.php"
const val LOOKUP_BY_ID = "lookup.php"

interface RecipeService {
    @GET(SEARCH_MEAL_BY_NAME_ENDPOINT)
    suspend fun searchMealByName(
        @Query("s") name: String
    ): ApiResponse

    @GET(LIST_MEALS_BY_FIRST_LETTER_ENDPOINT)
    suspend fun listMealsByFirstLetter(
        @Query("f") letter: Char
    ): ApiResponse

    @GET(LIST_CATEGORIES_ENDPOINT)
    suspend fun listCategories(): ApiResponse


    @GET(LIST_AREA_ENDPOINT)
    suspend fun listArea(): ApiResponse


    @GET(LIST_INGREDIENTS_ENDPOINT)
    suspend fun listIngredients(): ApiResponse


    @GET(FILTER_BY_MAIN_INGREDIENT)
    suspend fun filterByMAinIngredient(
        @Query("i") mainIng: String
    ): ApiResponse

    @GET(FILTER_BY_CATEGORY)
    suspend fun filterByCategory(
        @Query("c") category: String
    ): ApiResponse

    @GET(FILTER_BY_AREA)
    suspend fun filterByArea(
        @Query("a") area: String
    ): ApiResponse

    @GET(LOOKUP_BY_ID)
    suspend fun lookupById(
        @Query("i") id: String
    ): ApiResponse
}