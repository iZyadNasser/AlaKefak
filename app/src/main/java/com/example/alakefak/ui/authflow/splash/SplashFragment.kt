package com.example.alakefak.ui.authflow.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.databinding.FragmentSplashBinding
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.authflow.AuthActivity
import com.example.alakefak.ui.authflow.login.LoginFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var viewModel: SplashViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        val database = UserDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = SplashViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[SplashViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (AuthActivity.signedOut) {
            navigateToRegisterFragment()
        }
        setObservers()
    }

    private fun navigateToRegisterFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToRegisterFragment()
        findNavController().navigate(action)
    }

    private fun setObservers() {
        viewModel.users.observe(viewLifecycleOwner) {
            RecipeActivity.users = it

            CoroutineScope(Dispatchers.Main).launch {
                delay(2500)
                navigateBasedOnLoginStatus()
            }
        }
    }

    private fun navigateBasedOnLoginStatus() {
        val sharedPrefs =
            requireContext().getSharedPreferences(LoginFragment.PREFS_NAME, Context.MODE_PRIVATE)
        val isLoggedIn = sharedPrefs.getBoolean(LoginFragment.KEY_IS_LOGGED_IN, false)

        if (isLoggedIn) {
            navigateToRecipeActivity()
        } else {
            navigateToWelcomeFragment()
        }
    }

    private fun navigateToRecipeActivity() {
        val intent = Intent(requireContext(), RecipeActivity::class.java)
        intent.putExtra(LOGIN_SOURCE_PREF_NAME, LOGIN_SOURCE_SPLASH)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun navigateToWelcomeFragment() {
        val action = SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
        findNavController().navigate(action)
    }

    companion object {
        const val LOGIN_SOURCE_PREF_NAME = "source"
        const val LOGIN_SOURCE_SPLASH = "splash"
    }
}
