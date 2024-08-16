package com.example.alakefak.ui.authflow.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentLoginBinding
import com.example.alakefak.ui.authflow.validEmail
import com.example.alakefak.ui.authflow.validPassword

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
                val emailText = binding.textFieldEmail.editText?.text.toString()
                binding.textFieldEmail.helperText = validEmail(emailText, context)
            }
        }
    }

    private fun passwordFocusListener() {
        binding.textFieldPassword.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = binding.textFieldPassword.editText?.text.toString()
                binding.textFieldPassword.helperText = validPassword(passwordText, context)
            }
        }
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
            binding.loginBtn.setBackgroundColor(resources.getColor(R.color.md_theme_error))
        }

    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val passwordText = binding.textFieldPassword.editText?.text.toString()
                val emailText = binding.textFieldEmail.editText?.text.toString()
                binding.textFieldEmail.helperText = validEmail(emailText, context)
                binding.textFieldPassword.helperText = validPassword(passwordText, context)
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