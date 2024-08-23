package com.example.alakefak.ui.appflow.details

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentDetailsBinding
import com.example.alakefak.data.source.remote.model.Meal
class DetailsFragment(private val mealId: String?) : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDetailsBinding.bind(view)

        mealId?.let { id ->
            viewModel.getMeal(id)

            viewModel.notifyMealFetched.observe(viewLifecycleOwner) { meal ->
                meal?.let {
                    setupUI(it)
                }
            }
        } ?: run {

        }
    }

    private fun setupUI(meal: Meal) {
        binding.ingredients.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = IngredientAdapter(viewModel.covertIngredients(meal))
        }

        binding.FooodName.text = meal.strMeal
        binding.category.text = meal.strCategory
        binding.area.text = meal.strArea
        binding.instructiontext.text = meal.strInstructions

        Glide.with(this)
            .load(meal.strMealThumb)
            .into(binding.imageView)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
