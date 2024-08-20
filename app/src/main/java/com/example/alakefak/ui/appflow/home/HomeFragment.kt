package com.example.alakefak.ui.appflow.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.databinding.FragmentHomeBinding
import com.example.alakefak.ui.appflow.about.AboutFragment
import com.example.alakefak.ui.appflow.search.SearchFragment


class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: RecipesAdapter
    private lateinit var database: FavoritesDatabase
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        val infoMenu: ImageView = view.findViewById(R.id.infoMenu)
        infoMenu.setOnClickListener { view ->
            showPopup(view)
        }

    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.info_menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.signOut -> {
                    // code
                    true
                }
                R.id.aboutUs -> {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, AboutFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

}