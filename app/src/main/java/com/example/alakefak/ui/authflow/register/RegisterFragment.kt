package com.example.alakefak.ui.authflow.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.alakefak.R
import com.example.alakefak.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    val viewModel = RegisterFragmentViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()
        setupTextWatchers()
        handleOnClicks()
        singInForm()
    }

    private fun usernameFocusListener() {
        binding.userNameTextField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val usernameText = binding.userNameTextField.editText?.text.toString()
                binding.userNameTextField.helperText = viewModel.validUsername(usernameText, context)
            }
        }
    }

    private fun emailFocusListener() {
        binding.emailTextField.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val emailText = binding.emailTextField.editText?.text.toString()
                binding.emailTextField.helperText = viewModel.validEmail(emailText, context)
            }
        }
    }


    private fun passwordFocusListener() {
        binding.passwordTextField.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = binding.passwordTextField.editText?.text.toString()
                binding.passwordTextField.helperText = viewModel.validPassword(passwordText, context)
            }
        }
    }


    private fun confirmPasswordFocusListener() {
        binding.passwordConfirmFieldText.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = binding.passwordTextField.editText?.text.toString()
                val confirmPasswordText = binding.passwordConfirmFieldText.editText?.text.toString()
                binding.passwordConfirmFieldText.helperText = viewModel.validConfirmPassword(passwordText, confirmPasswordText, context)
            }
        }
    }

    private fun handleOnClicks() {
        binding.registerbtn.setOnClickListener { singInForm() }
    }


    private fun singInForm() {
        val validEmail = binding.emailTextField.helperText == null
        val validPassword = binding.passwordTextField.helperText == null
        val validConfirmPassword = binding.passwordConfirmFieldText.helperText == null
        val validUserName = binding.userNameTextField.helperText == null

        if (validEmail && validPassword && validConfirmPassword && validUserName) {
            signIn()
            binding.registerbtn.setBackgroundColor(resources.getColor(R.color.main_color))
        } else {
            disableSignIn()
            binding.registerbtn.setBackgroundColor(resources.getColor(R.color.button_disabled_color))
        }

    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val emailText = binding.emailTextField.editText?.text.toString()
                val usernameText = binding.userNameTextField.editText?.text.toString()
                val passwordText = binding.passwordTextField.editText?.text.toString()
                val confirmPasswordText = binding.passwordConfirmFieldText.editText?.text.toString()

                binding.emailTextField.helperText = viewModel.validEmail(emailText, context)
                binding.passwordTextField.helperText = viewModel.validPassword(passwordText, context)
                binding.passwordConfirmFieldText.helperText = viewModel.validConfirmPassword(passwordText, confirmPasswordText, context)
                binding.userNameTextField.helperText = viewModel.validUsername(usernameText, context)
                singInForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.emailTextField.editText?.addTextChangedListener(textWatcher)
        binding.passwordTextField.editText?.addTextChangedListener(textWatcher)
        binding.passwordConfirmFieldText.editText?.addTextChangedListener(textWatcher)
        binding.userNameTextField.editText?.addTextChangedListener(textWatcher)
    }


    private fun signIn() {
        binding.registerbtn.isEnabled = true
    }

    private fun disableSignIn() {
        binding.registerbtn.isEnabled = false
    }


}