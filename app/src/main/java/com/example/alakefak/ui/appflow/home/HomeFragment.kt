package com.example.alakefak.ui.appflow.home

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.data.source.remote.model.Meal
import com.example.alakefak.databinding.FragmentHomeBinding
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.appflow.about.AboutFragment
import com.example.alakefak.ui.appflow.details.DetailsFragment
import com.example.alakefak.ui.authflow.AuthActivity
import com.example.alakefak.ui.authflow.Utils
import com.example.alakefak.ui.authflow.login.LoginFragment.Companion.KEY_IS_LOGGED_IN
import com.example.alakefak.ui.authflow.login.LoginFragment.Companion.PREFS_NAME

class HomeFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
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

        val recipesRecyclerViewAdapter = bindRecipesRecyclerViewAdapter()

        handleRecipesRecyclerViewItemOnClick(recipesRecyclerViewAdapter)

        val categoriesRecyclerViewAdapter = bindCategoriesRecyclerViewAdapter()

        setStatsObservers(categoriesRecyclerViewAdapter, recipesRecyclerViewAdapter)

        binding.user.text = RecipeActivity.curUser?.userName

        val infoMenu: ImageView = view.findViewById(R.id.infoMenu)
        handleMenuOnClick(infoMenu)
    }


    private fun bindRecipesRecyclerViewAdapter(): RecipesAdapter {
        val recyclerView = binding.recyclerview
        val adapter = RecipesAdapter(emptyList(), viewModel)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.adapter = adapter
        return adapter
    }

    private fun handleRecipesRecyclerViewItemOnClick(recipesRecyclerViewAdapter: RecipesAdapter) {
        recipesRecyclerViewAdapter.setCommunicator(object : RecipesAdapter.Communicator {
            override fun onItemClicked(position: Int) {
                val clickedItem = recipesRecyclerViewAdapter.getItem(position)
                val bundle = passDataToDetailsFragment(clickedItem)
                clickedMeal = bundle
                navigateToDetailsFragment()
            }
        })
    }

    private fun bindCategoriesRecyclerViewAdapter(): CategoriesAdapter {
        val categoriesRecyclerView = binding.categoriesRecyclerView
        val categoriesRecyclerViewAdapter = CategoriesAdapter(emptyList(), viewModel)
        categoriesRecyclerView.adapter = categoriesRecyclerViewAdapter
        return categoriesRecyclerViewAdapter
    }

    private fun passDataToDetailsFragment(clickedItem: Meal): Bundle {
        val bundle = Bundle().apply {
            putString("MEAL_ID", clickedItem.idMeal)
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
            .setReorderingAllowed(true)
            .commit()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setStatsObservers(
        categoriesRecyclerViewAdapter: CategoriesAdapter,
        recipesRecyclerViewAdapter: RecipesAdapter
    ) {

        val categoriesLottieAnimationView = binding.categoriesLottieAnimationView

        viewModel.categoriesLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.categoriesRecyclerView.visibility = View.GONE
                categoriesLottieAnimationView.visibility = View.VISIBLE
                categoriesLottieAnimationView.playAnimation()
            } else {
                categoriesLottieAnimationView.cancelAnimation()
                categoriesLottieAnimationView.visibility = View.GONE
                binding.categoriesRecyclerView.visibility = View.VISIBLE
            }
        }

        val recipesLottieAnimationView = binding.recipesLottieAnimationView

        viewModel.recipesLoading.observe(viewLifecycleOwner) {
            if (it) {
                recipesLottieAnimationView.visibility = View.VISIBLE
                recipesLottieAnimationView.playAnimation()
            } else {
                recipesLottieAnimationView.cancelAnimation()
                recipesLottieAnimationView.visibility = View.GONE
            }
        }

        viewModel.categories.observe(viewLifecycleOwner) {
            categoriesRecyclerViewAdapter.updateItems(it)
        }

        viewModel.notifyDataChange.observe(viewLifecycleOwner) {
            recipesRecyclerViewAdapter.updateItems(viewModel.recipes)
        }
        viewModel.favAdded.observe(viewLifecycleOwner) {
            viewModel.getNewFavs()
        }
    }

    private fun handleMenuOnClick(infoMenu: ImageView) {
        infoMenu.setOnClickListener { view ->
            showPopup(view)
        }
    }

    private fun showPopup(view: View) {
        val popup = PopupMenu(requireContext(), view)
        popup.inflate(R.menu.info_menu)
        showIcons(popup)
        handleMenuItemsOnClick(popup)
        popup.show()
    }

    private fun showIcons(popup: PopupMenu) {
        try {
            val fields = popup.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popup)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod(
                        "setForceShowIcon",
                        Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun handleMenuItemsOnClick(popup: PopupMenu) {
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.signOut -> {
                    showSignOutDialogue()
                    true
                }

                R.id.aboutUs -> {
                    navigateToAboutFragment()
                    true
                }

                else -> false
            }
        }
    }

    private fun showSignOutDialogue() {
        Utils.showSignOutDialog(
            context = requireContext(),
            title = getString(R.string.sign_out),
            iconId = R.drawable.arrows_alogout_2,
            message = getString(R.string.sign_out_confirmation),
            positiveButtonText = getString(R.string.sign_out),
            negativeButtonText = getString(R.string.cancel),
        ) {
            navigateToRegisterFragment()
        }
    }

    private fun navigateToAboutFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_anim,
                R.anim.exit_anim,
                R.anim.pop_enter_anim,
                R.anim.pop_exit_anim
            )
            .replace(R.id.nav_host_fragment, AboutFragment())
            .addToBackStack(null)
            .commit()
    }


    private fun navigateToRegisterFragment() {
        val sharedPrefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, false)
        editor.apply()
        val intent = Intent(requireActivity(), AuthActivity::class.java)
        intent.putExtra(AuthActivity.SIGN_OUT_CHECK_INTENT_KEY, true)
        startActivity(intent)
        requireActivity().finish()
    }

    companion object {
        var clickedMeal: Bundle? = null
    }
}