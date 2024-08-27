package com.example.alakefak.ui.appflow.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.FavoritesDatabase
import com.example.alakefak.databinding.FragmentProfileBinding
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.authflow.AuthActivity
import com.example.alakefak.ui.authflow.Utils
import com.example.alakefak.ui.authflow.login.LoginFragment.Companion.KEY_IS_LOGGED_IN
import com.example.alakefak.ui.authflow.login.LoginFragment.Companion.PREFS_NAME

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
        handleSignOutOnClick()

    }


    private fun setStatsObservers() {
        viewModel.numOfFavorites.observe(viewLifecycleOwner) {
            binding.textNumFavsValue.text = it.toString()
        }

        viewModel.favoriteCategory.observe(viewLifecycleOwner) {
            binding.textFavCategoryValue.text = it
        }

        viewModel.favoriteArea.observe(viewLifecycleOwner) {
            binding.textFavAreaValue.text = it
        }

        viewModel.allFavorites.observe(viewLifecycleOwner) {
            viewModel.calculateFavorites()
        }
    }

    private fun handleSignOutOnClick() {
        binding.btnSignOut.setOnClickListener {
            showSignOutDialogue()
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

}