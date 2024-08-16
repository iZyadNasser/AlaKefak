package com.example.alakefak.ui.authflow.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()
        setupTextWatchers()
        handleOnClicks()
        singInForm()
    }

    private fun emailFocusListener() {
        binding.emailTextField.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.emailTextField.helperText = validEmail()
            }
        }
    }

    private fun validEmail(): String? {
        val emailText = binding.emailTextField.editText?.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return context?.getString(R.string.invalidEmailAddress)
        }
        return null
    }


    private fun passwordFocusListener() {
        binding.passwordTextField.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordTextField.helperText = validPassword()
            }
        }
    }

    private fun validPassword(): String? {
        val passwordText = binding.passwordTextField.editText?.text.toString()

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


    private fun confirmPasswordFocusListener() {
        binding.passwordConfirmFieldText.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                binding.passwordConfirmFieldText.helperText = validConfirmPassword()
            }
        }
    }

    private fun validConfirmPassword(): String? {
        val passwordText = binding.passwordTextField.editText?.text.toString()
        val confirmPasswordText = binding.passwordConfirmFieldText.editText?.text.toString()

        if (passwordText != confirmPasswordText) {
            return context?.getString(R.string.passwords_do_not_match)
        }
        return null
    }

    private fun handleOnClicks() {
        binding.registerbtn.setOnClickListener { singInForm() }
    }


    private fun singInForm() {
        val validEmail = binding.emailTextField.helperText == null
        val validPassword = binding.passwordTextField.helperText == null
        val validConfirmPassword = binding.passwordConfirmFieldText.helperText == null

        if (validEmail && validPassword && validConfirmPassword) {
            signIn()
            binding.registerbtn.setBackgroundColor(resources.getColor(R.color.main_color))
        } else {
            disableSignIn()
            binding.registerbtn.setBackgroundColor(resources.getColor(R.color.md_theme_error))


        }

    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.emailTextField.helperText = validEmail()
                binding.passwordTextField.helperText = validPassword()
                binding.passwordConfirmFieldText.helperText = validConfirmPassword()
                singInForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.emailTextField.editText?.addTextChangedListener(textWatcher)
        binding.passwordTextField.editText?.addTextChangedListener(textWatcher)
        binding.passwordConfirmFieldText.editText?.addTextChangedListener(textWatcher)
    }


    private fun signIn() {
        binding.registerbtn.isEnabled = true
    }

    private fun disableSignIn() {
        binding.registerbtn.isEnabled = false
    }


}