package com.example.alakefak.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritesInfo")
data class FavoritesInfo(
    @PrimaryKey var id: String,
    @ColumnInfo(name = "strMeal") val recipeName: String = "",
    @ColumnInfo(name = "strCategory") val recipeCategory: String = "",
    @ColumnInfo(name = "strMealThumb") val recipeImg: String = "",
    @ColumnInfo(name = "strArea") val recipeArea : String = "",
    @ColumnInfo(name = "idUser") val userId: Long
)
