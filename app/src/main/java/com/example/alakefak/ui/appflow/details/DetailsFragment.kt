package com.example.alakefak.ui.appflow.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentDetailsBinding
import com.example.alakefak.data.source.remote.model.Meal

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding
    private var meal: Meal = Meal()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.notifyMealFetched.observe(viewLifecycleOwner) {
            if (it != null) {
                meal = it
                binding.ingredients.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = IngredientAdapter(viewModel.covertIngredients(meal))
                }

                binding.FooodName.text = meal.strMeal
                binding.category.text = meal.strCategory
                binding.area.text = meal.strArea
                binding.instructiontext.text = meal.strInstructions


                Glide.with(this@DetailsFragment)
                    .load(meal.strMealThumb)
                    .into(binding.imageView)
            }
        }
    }

}

