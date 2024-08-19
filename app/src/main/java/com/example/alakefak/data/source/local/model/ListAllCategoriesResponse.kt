package com.example.alakefak.data.source.local.model

import com.google.gson.annotations.SerializedName

data class ListAllCategoriesResponse(
    @SerializedName("strCategory" )
    var strCategory : String? = null
)
