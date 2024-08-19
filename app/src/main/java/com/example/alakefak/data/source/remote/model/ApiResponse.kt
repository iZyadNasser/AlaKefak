package com.example.alakefak.data.source.remote.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("meals" )
    var meals : List<Meal?>? = null
)
