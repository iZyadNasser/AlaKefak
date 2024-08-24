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
import com.example.alakefak.ui.appflow.home.HomeFragment

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var binding: FragmentDetailsBinding
    private var meal: Meal = Meal()
    private var mealId: String = ""
    private var isPlayerViewVisible = false
    private var isExpanded = false

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        binding.playerView.settings.javaScriptEnabled = true


        mealId = HomeFragment.clickedMeal?.getString("MEAL_ID").toString()

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
            binding.imgYoutube.setOnClickListener {

                isPlayerViewVisible = !isPlayerViewVisible

                if (isPlayerViewVisible) {

                    binding.playerView.visibility = View.VISIBLE
                    val videoUrl = meal.strYoutube?.replace("watch?v=", "embed/")
                    videoUrl?.let { url ->
                        binding.playerView.loadUrl(url)
                    }
                } else {

                    binding.playerView.visibility = View.GONE
                }
            }

        }



        binding.readmore.setOnClickListener {
            if (isExpanded) {
                binding.instructions.maxLines = 4
                binding.readmore.text = getString(R.string.Read_more)
            } else {
                binding.instructions.maxLines = Int.MAX_VALUE
                binding.readmore.text = getString(R.string.read_less)
            }
            isExpanded = !isExpanded
        }



    }
}
