package com.example.alakefak.ui.appflow.details

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.databinding.FragmentDetailsBinding
import com.example.alakefak.ui.appflow.favorites.FavoritesAdapter.MyViewHolder
import com.example.alakefak.ui.appflow.home.HomeFragment

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var meal: Meal = Meal()
    private var mealId: String = ""
    private var isPlayerViewVisible = false
    private lateinit var database: FavoritesDatabase
    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FavoritesDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = DetailsViewModelFactory(database.favoritesDatabaseDao())
        viewModel = ViewModelProvider(this, viewModelFactory)[DetailsViewModel::class.java]
        binding.playerView.settings.javaScriptEnabled = true
        mealId = HomeFragment.clickedMeal?.getString("MEAL_ID").toString()
        viewModel.getMeal(mealId)

        setStatsObservers()
    }

    private fun setStatsObservers() {
        viewModel.notifyMealFetched.observe(viewLifecycleOwner) { fetchedMeal ->
            fetchedMeal?.let { meal ->
                bindDetailsData(meal)
            }
            handleVideoOnClick()
            handleHeartState()
            handleReadMoreOnClick()
        }
    }

    private fun bindDetailsData(meal: Meal): ViewTarget<ImageView, Drawable> {
        this.meal = meal
        binding.content.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = IngredientAdapter(viewModel.covertIngredients(meal))
        }
        binding.categoryInfo.text = meal.strCategory
        binding.areaInfo.text = meal.strArea
        binding.instructions.text = meal.strInstructions
        binding.foodname.text = meal.strMeal

        return Glide.with(this@DetailsFragment)
            .load(meal.strMealThumb)
            .error(R.drawable.error_placeholder)
            .into(binding.imgMealDetail)
    }

    private fun handleVideoOnClick() {
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

    private fun handleHeartState() {
        if (meal.isFavorite) {
            binding.btnSave.setImageResource(R.drawable.ic_heart_filled)
        } else {
            binding.btnSave.setImageResource(R.drawable.ic_heart_outline)
        }

        handleFavButtonOnClick()
    }

    private fun handleFavButtonOnClick() {
        binding.btnSave.setOnClickListener {
            if (meal.isFavorite) {
                showFavoriteConfirmationDialogue()
            } else {
                meal.isFavorite = true
                binding.btnSave.setImageResource(R.drawable.ic_heart_filled)
                viewModel.addToFav(meal)
            }
        }
    }
    private fun showFavoriteConfirmationDialogue() {
        val builder = AlertDialog.Builder(context)
        builder.apply {
            setTitle("Remove item from favorites")
            setMessage(context.getString(R.string.remove_favorites_confirmation))
            setPositiveButton(context.getString(R.string.remove)) { dialog, _ ->
                Toast.makeText(context,
                    context.getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                meal.isFavorite = false
                binding.btnSave.setImageResource(R.drawable.ic_heart_outline)
                viewModel.removeFromFav(meal)
            }
            setNegativeButton(R.string.cancel) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }
    private fun handleReadMoreOnClick() {
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