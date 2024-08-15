package com.example.alakefak.ui.authflow.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alakefak.databinding.FragmentSplashBinding
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.fragment.findNavController
import com.example.alakefak.R


class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashBinding.inflate(layoutInflater)
        val swipeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.swipe_out)

        swipeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.yourImageViewId.visibility = View.GONE

                val action =
                    SplashFragmentDirections.actionSplashFragmentToWelcomeFragment()
                findNavController().navigate(action)
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

        Handler(Looper.getMainLooper()).postDelayed({
            binding.yourImageViewId.startAnimation(swipeOut)
        }, 2000)

        return binding.root
    }
}
