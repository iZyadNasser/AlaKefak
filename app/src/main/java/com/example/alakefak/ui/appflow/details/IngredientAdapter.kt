package com.example.alakefak.ui.appflow.details


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.data.source.remote.model.Ingredient
import com.example.alakefak.databinding.ItemIngredientBinding

class IngredientAdapter (private var ingredients: List<Ingredient> = emptyList()): RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val binding = ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return IngredientViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    class IngredientViewHolder(private val binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ingredient:Ingredient) {
            binding.ingredient.text = ingredient.ingredientName
            binding.measure.text = ingredient.measureName
        }
    }

}
