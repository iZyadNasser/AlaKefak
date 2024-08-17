package com.example.alakefak.data.source.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favoritesInfo")
data class FavoritesInfo(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "strMeal") val recipeName: String,
    @ColumnInfo(name = "strCategory") val recipeCategory: String,
    @ColumnInfo(name = "strMealThumb") val recipeImg: String
)
