package com.example.alakefak.data.source.local.model

import com.google.gson.annotations.SerializedName

data class ListAllIngredientsResponse(
    @SerializedName("idIngredient")
    var idIngredient: String? = null,
    @SerializedName("strIngredient")
    var strIngredient: String? = null,
    @SerializedName("strDescription")
    var strDescription: String? = null,
    @SerializedName("strType")
    var strType: String? = null
)
