package com.example.alakefak.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIModule {
    val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    const val SEARCH_MEAL_BY_NAME_ENDPOINT = "search.php?s=Arrabiata"
    const val LIST_MEALS_BY_FIRST_LETTER_ENDPOINT = "search.php?f=a"
    const val LIST_CATEGORIES_ENDPOINT = "list.php?c=list"
    const val LIST_AREA_ENDPOINT = "list.php?a=list"
    const val LIST_INGREDIENTS_ENDPOINT = "list.php?i=list"
    const val FILTER_BY_MAIN_INGREDIENT = "filter.php?i=chicken_breast"
    const val FILTER_BY_CATEGORY = "filter.php?c=Seafood"
    const val FILTER_BY_AREA = "filter.php?a=Canadian"


    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiServise= retrofit.create(RecipeService::class.java)

}