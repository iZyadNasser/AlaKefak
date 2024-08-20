package com.example.alakefak.ui.appflow.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: RecipesAdapter
    private lateinit var database: FavoritesDatabase
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        database = FavoritesDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = HomeViewModelFactory(database.favoritesDatabaseDao())
        viewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = RecipesAdapter(emptyList(),database.favoritesDatabaseDao())
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter


    }

}