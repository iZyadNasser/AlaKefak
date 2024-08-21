package com.example.alakefak.ui.appflow.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alakefak.databinding.FragmentFavoritesBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.FavoritesDatabase


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
        viewModel = ViewModelProvider(this, viewModelFactory)[FavoritesFragmentViewModel::class.java]
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        viewModel.favorite.observe(viewLifecycleOwner, Observer { favoriteItems ->
            adapter.setupItems(favoriteItems)
        })

        viewModel.getAllItems()
    }

    private fun setUpRecyclerView(){
        adapter = FavoritesAdapter(emptyList(),database.favoritesDatabaseDao())
        binding.favoritesRecyclerView.adapter = adapter
    }
}