package com.example.alakefak.ui.appflow.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentDetailsBinding
import com.example.alakefak.data.source.remote.model.Meal

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding
    private var meal: Meal = Meal()
    private var mealId: String = ""

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        binding.playerView.settings.javaScriptEnabled = true


        mealId = arguments?.getString("MEAL_ID") ?: ""

        viewModel.getMeal(mealId)
        viewModel.notifyMealFetched.observe(viewLifecycleOwner) { fetchedMeal ->
            fetchedMeal?.let { meal ->
                this.meal = meal
                binding.content.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = IngredientAdapter(viewModel.covertIngredients(meal))
                }

                binding.categoryInfo.text = meal.strCategory
                binding.areaInfo.text = meal.strArea
                binding.instructions.text = meal.strInstructions
                binding.foodname.text = meal.strMeal

                Glide.with(this@DetailsFragment)
                    .load(meal.strMealThumb)
                    .into(binding.imgMealDetail)
            }

        }


    }
}
