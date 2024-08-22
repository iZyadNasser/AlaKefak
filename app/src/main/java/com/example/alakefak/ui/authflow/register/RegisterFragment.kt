package com.example.alakefak.ui.authflow.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.data.source.local.model.User
import com.example.alakefak.databinding.FragmentRegisterBinding
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.authflow.ErrorStates
import com.example.alakefak.ui.authflow.FormUtils
import com.example.alakefak.ui.authflow.FormUtils.validConfirmPassword
import com.example.alakefak.ui.authflow.FormUtils.validEmail
import com.example.alakefak.ui.authflow.FormUtils.validPassword
import com.example.alakefak.ui.authflow.FormUtils.validUsername

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val database = UserDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = RegisterFragmentViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFormState()
        usernameFocusListener()
        emailFocusListener()
        passwordFocusListener()
        confirmPasswordFocusListener()
        setupTextWatchers()
        handleOnClicks()
        singInForm()
    }

    private fun observeFormState() {
        viewModel.errorState.observe(viewLifecycleOwner) {
            when (viewModel.errorState.value) {
                ErrorStates.EMAIL -> {
                    Toast.makeText(
                        this.context,
                        getString(R.string.this_email_is_used_in_an_already_existing_account),
                        Toast.LENGTH_LONG
                    ).show()
                }

                ErrorStates.USERNAME -> {
                    Toast.makeText(
                        this.context,
                        getString(R.string.this_username_is_already_taken),
                        Toast.LENGTH_LONG
                    ).show()
                }

                ErrorStates.BOTH -> {
                    Toast.makeText(
                        this.context,
                        getString(R.string.this_account_already_exists_do_you_want_to_sign_in),
                        Toast.LENGTH_LONG
                    ).show()
                }

                ErrorStates.NONE -> {
//                    val intent = Intent(activity, RecipeActivity::class.java)
//                    intent.putExtra(FormUtils.INTENT_KEY, viewModel.user)
//                    startActivity(intent)
//                    activity?.finish()

                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(binding.emailTextField.editText?.text.toString(), binding.passwordTextField.editText?.text.toString()))
                }

                else -> {}
            }
        }
    }

    private fun usernameFocusListener() {
        binding.userNameTextField.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val usernameText = binding.userNameTextField.editText?.text.toString()
                binding.userNameTextField.helperText = validUsername(usernameText, context)
            }
        }
    }

    private fun emailFocusListener() {
        binding.emailTextField.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val emailText = binding.emailTextField.editText?.text.toString()
                binding.emailTextField.helperText = validEmail(emailText, context)
            }
        }
    }


    private fun passwordFocusListener() {
        binding.passwordTextField.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = binding.passwordTextField.editText?.text.toString()
                binding.passwordTextField.helperText = validPassword(passwordText, context)
            }
        }
    }


    private fun confirmPasswordFocusListener() {
        binding.passwordConfirmFieldText.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = binding.passwordTextField.editText?.text.toString()
                val confirmPasswordText = binding.passwordConfirmFieldText.editText?.text.toString()
                binding.passwordConfirmFieldText.helperText = validConfirmPassword(passwordText, confirmPasswordText, context)
            }
        }
    }

    private fun handleOnClicks() {
        binding.signInTextView.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment("", ""))
        }
        binding.registerbtn.setOnClickListener {
            handleUserData()
            singInForm()
        }
    }

    private fun handleUserData() {
        val user = User(
            id = 0,
            userName = binding.userNameTextField.editText?.text.toString(),
            email = binding.emailTextField.editText?.text.toString(),
            password = binding.passwordTextField.editText?.text.toString()
        )

        viewModel.handleUserData(user)
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

                binding.emailTextField.helperText = validEmail(emailText, context)
                binding.passwordTextField.helperText = validPassword(passwordText, context)
                binding.passwordConfirmFieldText.helperText = validConfirmPassword(passwordText, confirmPasswordText, context)
                binding.userNameTextField.helperText = validUsername(usernameText, context)
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