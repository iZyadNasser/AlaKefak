package com.example.alakefak.ui.appflow.details

import androidx.lifecycle.ViewModel
import com.example.alakefak.data.source.remote.model.Ingredient
import com.example.alakefak.data.source.remote.model.Meal

class DetailsViewModel () : ViewModel() {

    fun covertIngredients(meal: Meal): List<Ingredient> {
        val ingredients = listOf<Ingredient>(
            Ingredient(
                meal.strIngredient1?:"",
                meal.strMeasure1?:""
            ),
            Ingredient(
                meal.strIngredient2?:"",
                meal.strMeasure2?:""
            ),
            Ingredient(
                meal.strIngredient3?:"",
                meal.strMeasure3?:""
            ),
            Ingredient(
                meal.strIngredient4?:"",
                meal.strMeasure4?:""
            ),
            Ingredient(
                meal.strIngredient5?:"",
                meal.strMeasure5?:""
            ),
            Ingredient(
                meal.strIngredient6?:"",
                meal.strMeasure6?:""
            ),
            Ingredient(
                meal.strIngredient7?:"",
                meal.strMeasure7?:""
            ),
            Ingredient(
                meal.strIngredient8?:"",
                meal.strMeasure8?:""
            ),
            Ingredient(
                meal.strIngredient9?:"",
                meal.strMeasure9?:""
            ),
            Ingredient(
                meal.strIngredient10?:"",
                meal.strMeasure10?:""
            ),
            Ingredient(
                meal.strIngredient11?:"",
                meal.strMeasure11?:""
            ),
            Ingredient(
                meal.strIngredient12?:"",
                meal.strMeasure12?:""
            ),
            Ingredient(
                meal.strIngredient13?:"",
                meal.strMeasure13?:""
            ),
            Ingredient(
                meal.strIngredient14?:"",
                meal.strMeasure14?:""
            ),
            Ingredient(
                meal.strIngredient15?:"",
                meal.strMeasure15?:""
            ),
            Ingredient(
                meal.strIngredient16?:"",
                meal.strMeasure16?:""
            ),
            Ingredient(
                meal.strIngredient17?:"",
                meal.strMeasure17?:""
            ),
            Ingredient(
                meal.strIngredient18?:"",
                meal.strMeasure18?:""
            ),
            Ingredient(
                meal.strIngredient19?:"",
                meal.strMeasure19?:""
            ),
            Ingredient(
                meal.strIngredient20?:"",
                meal.strMeasure20?:""
            )
        )
        val filteredIngredients = ingredients.filter { it.ingredientName.isNotEmpty() }
        return filteredIngredients
    }
}