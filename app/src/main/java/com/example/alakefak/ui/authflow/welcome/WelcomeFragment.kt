package com.example.alakefak.ui.authflow.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alakefak.databinding.FragmentWelcomeBinding
import com.example.alakefak.ui.authflow.AuthActivity

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    private val navOptions = AuthActivity.navOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                requireActivity().finish()
            }
        })

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.buttonGetStarted.setOnClickListener {
            navigateToRegisterFragment()
        }
    }

    private fun navigateToRegisterFragment() {
        val action = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment()
        findNavController().navigate(action, navOptions)
    }

}
