package com.example.alakefak.ui.appflow.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.databinding.FragmentProfileBinding
import com.example.alakefak.ui.appflow.RecipeActivity

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var database: FavoritesDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        database = FavoritesDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = ProfileViewModelFactory(database.favoritesDatabaseDao())
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfileViewModel::class.java]
        binding.userName.text = RecipeActivity.curUser?.userName
        binding.email.text = RecipeActivity.curUser?.email
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setStatsObservers()

    }

    private fun setStatsObservers() {
        viewModel.numOfFavorites.observe(viewLifecycleOwner) {
            binding.textNumFavsValue.text = it.toString()
        }

        viewModel.favoriteCategory.observe(viewLifecycleOwner) {
            binding.textFavCategory.text = it
        }

        viewModel.favoriteArea.observe(viewLifecycleOwner) {
            binding.textFavAreaValue.text = it
        }

        viewModel.allFavorites.observe(viewLifecycleOwner) {
           // viewModel.calculateFavorites()
        }
    }
}