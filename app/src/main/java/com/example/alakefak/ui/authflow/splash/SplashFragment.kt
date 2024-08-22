package com.example.alakefak.ui.authflow.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alakefak.databinding.FragmentSplashBinding
import com.example.alakefak.ui.authflow.AuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(layoutInflater)

        if (AuthActivity.signedOut) {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRegisterFragment())
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            delay(2500)
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToWelcomeFragment())
        }
    }

}
