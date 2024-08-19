package com.example.alakefak.data.source.local.model

import com.google.gson.annotations.SerializedName

data class FilterByMainIngredientResponse(
    @SerializedName("strMeal")
    var strMeal: String? = null,
    @SerializedName("strMealThumb")
    var strMealThumb: String? = null,
    @SerializedName("idMeal")
    var idMeal: String? = null
)
