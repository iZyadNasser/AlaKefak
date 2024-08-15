package com.example.alakefak.ui.authflow.welcome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.button.setOnClickListener {
            setNavigationWithAnimation()
        }
    }
    private fun setNavigationWithAnimation() {
        val swipeOut = AnimationUtils.loadAnimation(requireContext(), R.anim.swipe_out_short)

        swipeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
            }

            override fun onAnimationEnd(animation: Animation?) {
                binding.root.visibility = View.GONE

                findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment())
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

        Handler(Looper.getMainLooper()).postDelayed({
            binding.root.startAnimation(swipeOut)
        }, 5)
    }
}
