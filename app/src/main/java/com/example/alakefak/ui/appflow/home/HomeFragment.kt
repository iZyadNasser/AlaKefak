package com.example.alakefak.ui.appflow.home

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.databinding.FragmentHomeBinding
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.appflow.about.AboutFragment
import com.example.alakefak.ui.appflow.search.SearchFragment
import com.example.alakefak.ui.authflow.AuthActivity
import com.example.alakefak.ui.authflow.FormUtils


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

        val categoriesRecyclerView = binding.categoriesRecyclerView
        val categoriesRecyclerViewAdapter = CategoriesAdapter(emptyList(), viewModel)
        categoriesRecyclerView.adapter = categoriesRecyclerViewAdapter

        viewModel.categories.observe(viewLifecycleOwner) {
            categoriesRecyclerViewAdapter.updateItems(it)
        }

        viewModel.recipes.observe(viewLifecycleOwner) {
            categoriesRecyclerViewAdapter.updateItems(viewModel.categories.value!!)
            adapter.updateItems(it)
        }

        binding.user.text = RecipeActivity.curUser?.userName

        val infoMenu: ImageView = view.findViewById(R.id.infoMenu)
        infoMenu.setOnClickListener { view ->
            showPopup(view)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.info_menu)

        // Force icons to show
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.signOut -> {
                    showSignOutDialogue()
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

    private fun showSignOutDialogue() {
        val builder = context?.let { AlertDialog.Builder(it) }
        builder?.apply {
            setMessage("Are you sure you want to sign out?")
            setPositiveButton("Sign out") { dialog, _ ->
                Toast.makeText(context, "Signed out successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
                navigateToRegisterFragment()
            }
            setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }

    }
    private fun navigateToRegisterFragment() {
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        intent.putExtra("key", true)
        startActivity(intent)
        requireActivity().finish()
    }


}