package com.example.alakefak.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritesInfo")
data class FavoritesInfo(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "strMeal") val recipeName: String = "",
    @ColumnInfo(name = "strCategory") val recipeCategory: String = "",
    @ColumnInfo(name = "strMealThumb") val recipeImg: String ="https://www.themealdb.com/images/media/meals/wvpsxx1468256321.jpg",
    @ColumnInfo(name = "strArea") val area : String = ""
)
