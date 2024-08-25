package com.example.alakefak.ui.appflow.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.local.model.FavoritesInfo
import com.example.alakefak.databinding.FragmentFavoritesBinding
import com.example.alakefak.ui.appflow.details.DetailsFragment
import com.example.alakefak.ui.appflow.home.HomeFragment.Companion.clickedMeal


class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var adapter: FavoritesAdapter
    private lateinit var viewModel: FavoritesFragmentViewModel
    private lateinit var database: FavoritesDatabase
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(layoutInflater)
        database = FavoritesDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = FavoritesFragmentViewModelFactory(database.favoritesDatabaseDao())
        viewModel =
            ViewModelProvider(this, viewModelFactory)[FavoritesFragmentViewModel::class.java]
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        handleFavoritesRecyclerViewItemOnClick()
        setStatsObservers()
        viewModel.getAllItems()

    }

    private fun setUpRecyclerView() {
        adapter = FavoritesAdapter(emptyList(), database.favoritesDatabaseDao())
        binding.favoritesRecyclerView.adapter = adapter
    }

    private fun handleFavoritesRecyclerViewItemOnClick() {
        adapter.setCommunicator(object : FavoritesAdapter.Communicator {
            override fun onItemClicked(position: Int) {
                val clickedItem = adapter.getItem(position)
                val bundle = passDataToDetailsFragment(clickedItem)
                clickedMeal = bundle
                navigateToDetailsFragment()
            }
        })
    }

    private fun passDataToDetailsFragment(clickedItem: FavoritesInfo): Bundle {
        val bundle = Bundle().apply {
            putString("MEAL_ID", clickedItem.id)
        }
        return bundle
    }

    private fun navigateToDetailsFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_anim,
                R.anim.exit_anim,
                R.anim.pop_enter_anim,
                R.anim.pop_exit_anim
            )
            .replace(R.id.nav_host_fragment, DetailsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun setStatsObservers() {
        viewModel.favorite.observe(viewLifecycleOwner, Observer { favoriteItems ->
            adapter.setupItems(favoriteItems)
        })
    }

}