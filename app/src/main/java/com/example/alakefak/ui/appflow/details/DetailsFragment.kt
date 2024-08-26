package com.example.alakefak.ui.appflow.details

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.ViewTarget
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.databinding.FragmentDetailsBinding
import com.example.alakefak.ui.appflow.home.HomeFragment
import com.android.volley.Response
import com.example.alakefak.ui.authflow.Utils


class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var viewModel: DetailsViewModel
    private lateinit var binding: FragmentDetailsBinding
    private var meal: Meal = Meal()
    private var mealId: String = ""
    private var isPlayerViewVisible = false
    private lateinit var database: FavoritesDatabase
    private var isExpanded = false
    private lateinit var requestQueue: RequestQueue

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        requestQueue = Volley.newRequestQueue(requireContext())

        setStatsObservers()
    }

    private fun setStatsObservers() {
        val lottieView = binding.lottieAnimationView
        viewModel.pageLoading.observe(viewLifecycleOwner) {
            if (it) {
                lottieView.visibility = View.VISIBLE
                lottieView.playAnimation()
            } else {
                lottieView.cancelAnimation()
                lottieView.visibility = View.GONE
            }
        }

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

    private fun checkVideoAvailability(videoUrl: String, callback: (Boolean) -> Unit) {
        val url = "https://www.youtube.com/oembed?url=$videoUrl&format=json"
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            {
                callback(true)
            },
            {
                callback(false)
            }
        )
        requestQueue.add(request)
    }


    private fun handleVideoOnClick() {
        checkVideoAvailability(meal.strYoutube ?: "") { isAvailable ->
            if (isAvailable) {
                binding.imgYoutube.visibility = View.VISIBLE
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
            } else {
                binding.imgYoutube.visibility = View.GONE
            }
        }
    }



    private fun handleHeartState() {
        if (meal.isFavorite) {
            binding.btnSave.setImageResource(R.drawable.ic_heart_filled)
            animateHeart(binding.btnSave)
        } else {
            binding.btnSave.setImageResource(R.drawable.ic_heart_outline)
            animateHeart(binding.btnSave)
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
                animateHeart(binding.btnSave)
                viewModel.addToFav(meal)
            }
        }
    }

    private fun showFavoriteConfirmationDialogue() {
        Utils.showSignOutDialog(
            context = requireContext(),
            title = getString(R.string.remove_item_from_favorites),
            message = getString(R.string.remove_favorites_confirmation),
            positiveButtonText = getString(R.string.remove),
            negativeButtonText = getString(R.string.cancel)
        ) {
            meal.isFavorite = false
            binding.btnSave.setImageResource(R.drawable.ic_heart_outline)
            animateHeart(binding.btnSave)
            viewModel.removeFromFav(meal)
        }
    }

    private fun animateHeart(heartBtn: ImageButton) {
        val scaleX = ObjectAnimator.ofFloat(heartBtn, "scaleX", 0.8f, 1.2f, 1.0f)
        val scaleY = ObjectAnimator.ofFloat(heartBtn, "scaleY", 0.8f, 1.2f, 1.0f)
        AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            duration = 300
            start()
        }
    }
    private fun handleReadMoreOnClick() {
        setupReadMoreButton()

        binding.readmore.setOnClickListener {
            toggleReadMore()
        }
    }

    private fun setupReadMoreButton() {
        binding.instructions.post {
            val lineCount = binding.instructions.lineCount
            if (lineCount < 4) {
                binding.readmore.visibility = View.GONE
            } else {
                binding.readmore.visibility = View.VISIBLE
            }
        }
    }

    private fun toggleReadMore() {
        if (isExpanded) {
            collapseText()
        } else {
            expandText()
        }
        isExpanded = !isExpanded
    }

    private fun collapseText() {
        binding.instructions.maxLines = 4
        binding.readmore.text = getString(R.string.Read_more)
    }

    private fun expandText() {
        binding.instructions.maxLines = Int.MAX_VALUE
        binding.readmore.text = getString(R.string.read_less)
    }


}