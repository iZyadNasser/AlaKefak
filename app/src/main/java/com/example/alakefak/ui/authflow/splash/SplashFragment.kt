package com.example.alakefak.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentSplashBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_splash, container, false
        )

        val swipeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.swipe_out)

        swipeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.yourImageViewId.visibility = View.GONE

                findNavController().navigate(SplashFragmentDirections.actionWelcomeFragmentToRegisterFragment())
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })



        return binding.root
    }
}
