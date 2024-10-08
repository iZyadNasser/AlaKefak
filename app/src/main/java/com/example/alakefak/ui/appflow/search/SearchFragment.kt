package com.example.alakefak.ui.appflow.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentSearchBinding
import com.example.alakefak.ui.appflow.details.DetailsFragment
import com.example.alakefak.ui.appflow.home.HomeFragment.Companion.clickedMeal
import com.example.alakefak.ui.appflow.home.RecipesAdapter

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

        viewModel.searchResults.observe(viewLifecycleOwner, Observer { results ->
            adapter.setItems(results.toList())


            if (results.isEmpty()) {
                binding.textViewEmptySearch.visibility = View.VISIBLE
                binding.searchLottieAnimationView.visibility = View.VISIBLE
                binding.searchLottieAnimationView.playAnimation()
            } else {
                binding.textViewEmptySearch.visibility = View.GONE
                binding.searchLottieAnimationView.cancelAnimation()
                binding.searchLottieAnimationView.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })
        adapter.setCommunicator(object : SearchFragmentRecyclerViewAdapter.Communicator {
            override fun onItemClicked(position: Int) {
                val clickedItem = adapter.getItem(position)
                val bundle = Bundle().apply {
                    putString("MEAL_ID", clickedItem.id)
                }
                clickedMeal = bundle
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
                //findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
            }
        })

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.searchView.clearFocus()
                viewModel.search(query ?: "")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    binding.resultsTextView.text = getString(R.string.empty_text)
                    binding.textViewEmptySearch.text = getString(R.string.search_for_recipes)
                    binding.textViewEmptySearch.visibility = View.VISIBLE
                    binding.searchLottieAnimationView.visibility = View.VISIBLE
                    binding.searchLottieAnimationView.playAnimation()
                    binding.recyclerView.visibility = View.GONE
                } else {

                    binding.resultsTextView.text = getString(R.string.results)
                    binding.textViewEmptySearch.text = getString(R.string.oops)
                    binding.textViewEmptySearch.visibility = View.GONE
                    binding.searchLottieAnimationView.cancelAnimation()
                    binding.searchLottieAnimationView.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                }
                viewModel.search(newText ?: "")
                return false
            }
        })
    }
}