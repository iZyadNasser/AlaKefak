package com.example.alakefak.ui.authflow.register

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
import com.example.alakefak.databinding.FragmentRegisterBinding
import com.example.alakefak.ui.authflow.splash.SplashFragmentDirections

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(layoutInflater)

        setOnClickListeners()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setOnClickListeners() {
        binding.loginTextView.setOnClickListener {
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

                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }

            override fun onAnimationRepeat(p0: Animation?) {

            }

        })

        Handler(Looper.getMainLooper()).postDelayed({
            binding.root.startAnimation(swipeOut)
        }, 5)
    }
}