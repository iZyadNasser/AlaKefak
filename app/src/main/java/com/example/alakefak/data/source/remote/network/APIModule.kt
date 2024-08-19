package com.example.alakefak.data.source.remote.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIModule {
    private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: RecipeService = retrofit.create(RecipeService::class.java)

}