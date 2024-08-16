package com.example.alakefak.ui.authflow.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailFocusListener()
        passwordFocusListener()
        setupTextWatchers()
        handleOnClicks()
        singInForm()
    }

    private fun emailFocusListener() {
        binding.textFieldEmail.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.textFieldEmail.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.textFieldEmail.editText?.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return context?.getString(R.string.invalidEmailAddress)
        }
        return null
    }


    private fun passwordFocusListener() {
        binding.textFieldPassword.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.textFieldPassword.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.textFieldPassword.editText?.text.toString()

        if (passwordText.length < 8) {
            return context?.getString(R.string.minNumberOfChars)
        }
        if (!passwordText.matches(".*[A-Z].*".toRegex())) {
            return (context?.getString(R.string.minNumberOfUpperChars))

        }
        if (!passwordText.matches(".*[a-z].*".toRegex())) {
            return (context?.getString(R.string.minNumberOfLowerChars))

        }
        if (!passwordText.matches(".*[0-9].*".toRegex())) {
            return (context?.getString(R.string.minNumberOfDigits))

        }
        if (!passwordText.matches(".*[@#\$%^+=].*".toRegex())) {
            return (context?.getString(R.string.minNumberOfSymbols))

        }

        return null
    }

    private fun handleOnClicks() {
        binding.loginBtn.setOnClickListener { singInForm() }
    }


    private fun singInForm() {
        val validEmail = binding.textFieldEmail.helperText == null
        val validPassword = binding.textFieldPassword.helperText == null

        if (validEmail && validPassword) {
            signIn()
            binding.loginBtn.setBackgroundColor(resources.getColor(R.color.main_color))
        } else {
            disableSignIn()
            binding.loginBtn.setBackgroundColor(resources.getColor(R.color.button_disabled_color))


        }

    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.textFieldEmail.helperText = validEmail()
                binding.textFieldPassword.helperText = validPassword()
                singInForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.textFieldEmail.editText?.addTextChangedListener(textWatcher)
        binding.textFieldPassword.editText?.addTextChangedListener(textWatcher)
    }


    private fun signIn() {
        binding.loginBtn.isEnabled = true
    }

    private fun disableSignIn() {
        binding.loginBtn.isEnabled = false
    }


}