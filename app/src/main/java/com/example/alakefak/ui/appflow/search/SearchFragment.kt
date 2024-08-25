package com.example.alakefak.ui.appflow.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R
import com.example.alakefak.data.source.local.model.SearchResult
import com.example.alakefak.databinding.FragmentSearchBinding
import com.example.alakefak.ui.appflow.details.DetailsFragment
import com.example.alakefak.ui.appflow.home.HomeFragment.Companion.clickedMeal

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SearchFragmentRecyclerViewAdapter
    private val viewModel: SearchFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        searchView = binding.searchView
        recyclerView = binding.recyclerView
        adapter = SearchFragmentRecyclerViewAdapter(ArrayList())
        recyclerView.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setStatsObservers()
        handleSearchRecyclerViewItemOnClick()
        handleSearchListener()
    }

    private fun setStatsObservers() {
        viewModel.searchResults.observe(viewLifecycleOwner, Observer {
            adapter.setItems(it.toList())
        })
    }

    private fun handleSearchRecyclerViewItemOnClick() {
        adapter.setCommunicator(object : SearchFragmentRecyclerViewAdapter.Communicator {
            override fun onItemClicked(position: Int) {
                val clickedItem = adapter.getItem(position)
                val bundle = passDataToDetailsFragment(clickedItem)
                clickedMeal = bundle
                navigateToDetailsFragment()
            }
        })
    }

    private fun passDataToDetailsFragment(clickedItem: SearchResult): Bundle {
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

    private fun handleSearchListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                viewModel.search(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.resultsTextView.text = getString(R.string.empty_text)
                } else {
                    binding.resultsTextView.text = getString(R.string.results)
                }
                viewModel.search(newText ?: "")
                return false
            }
        })
    }

}