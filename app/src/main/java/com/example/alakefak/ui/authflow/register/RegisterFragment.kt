package com.example.alakefak.ui.authflow.register

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.data.source.local.model.User
import com.example.alakefak.databinding.FragmentRegisterBinding
import com.example.alakefak.ui.authflow.AuthActivity
import com.example.alakefak.ui.authflow.ErrorStates
import com.example.alakefak.ui.authflow.Utils.validConfirmPassword
import com.example.alakefak.ui.authflow.Utils.validEmail
import com.example.alakefak.ui.authflow.Utils.validPassword
import com.example.alakefak.ui.authflow.Utils.validUsername
import com.google.android.material.textfield.TextInputLayout

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: RegisterFragmentViewModel
    private lateinit var emailView: TextInputLayout
    private lateinit var usernameView: TextInputLayout
    private lateinit var passwordView: TextInputLayout
    private lateinit var confirmPasswordView: TextInputLayout
    private lateinit var registerBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val database = UserDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = RegisterFragmentViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[RegisterFragmentViewModel::class.java]
        initializeViews()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (AuthActivity.signedOut) {
                    requireActivity().finish()
                } else {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }
        })

        return binding.root
    }

    private fun initializeViews() {
        emailView = binding.textFieldEmailRegister
        usernameView = binding.textFieldUsername
        passwordView = binding.textFieldPasswordRegister
        confirmPasswordView = binding.textFieldConfirmPassword
        registerBtn = binding.btnRegister
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
                    findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(
                            emailView.editText?.text.toString(),
                            passwordView.editText?.text.toString()
                        )
                    )
                }

                else -> {}
            }
        }
    }

    private fun usernameFocusListener() {
        usernameView.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val usernameText = usernameView.editText?.text.toString()
                usernameView.helperText = validUsername(usernameText, context)
            }
        }
    }

    private fun emailFocusListener() {
        emailView.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val emailText = emailView.editText?.text.toString()
                emailView.helperText = validEmail(emailText, context)
            }
        }
    }


    private fun passwordFocusListener() {
        passwordView.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = passwordView.editText?.text.toString()
                passwordView.helperText = validPassword(passwordText, context)
            }
        }
    }


    private fun confirmPasswordFocusListener() {
        confirmPasswordView.editText?.setOnFocusChangeListener { _, focused ->
            if (!focused) {
                val passwordText = passwordView.editText?.text.toString()
                val confirmPasswordText = confirmPasswordView.editText?.text.toString()
                confirmPasswordView.helperText =
                    validConfirmPassword(passwordText, confirmPasswordText, context)
            }
        }
    }

    private fun handleOnClicks() {
        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(
                    "",
                    ""
                )
            )
        }
        registerBtn.setOnClickListener {
            handleUserData()
            singInForm()
        }
    }

    private fun handleUserData() {
        val user = User(
            id = 0,
            userName = usernameView.editText?.text.toString(),
            email = emailView.editText?.text.toString(),
            password = passwordView.editText?.text.toString()
        )
        viewModel.handleUserData(user)
    }

    private fun singInForm() {
        val validEmail = emailView.helperText == null
        val validPassword = passwordView.helperText == null
        val validConfirmPassword = confirmPasswordView.helperText == null
        val validUserName = usernameView.helperText == null

        if (validEmail && validPassword && validConfirmPassword && validUserName) {
            signIn()
            registerBtn.setBackgroundColor(resources.getColor(R.color.main_color))
        } else {
            disableSignIn()
            registerBtn.setBackgroundColor(resources.getColor(R.color.button_disabled_color))
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                emailView.helperText = validEmail(emailView.editText?.text.toString(), context)
                passwordView.helperText =
                    validPassword(passwordView.editText?.text.toString(), context)
                confirmPasswordView.helperText =
                    validConfirmPassword(
                        passwordView.editText?.text.toString(),
                        confirmPasswordView.editText?.text.toString(),
                        context
                    )
                usernameView.helperText =
                    validUsername(usernameView.editText?.text.toString(), context)
                singInForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        emailView.editText?.addTextChangedListener(textWatcher)
        passwordView.editText?.addTextChangedListener(textWatcher)
        confirmPasswordView.editText?.addTextChangedListener(textWatcher)
        usernameView.editText?.addTextChangedListener(textWatcher)
    }


    private fun signIn() {
        registerBtn.isEnabled = true
    }

    private fun disableSignIn() {
        registerBtn.isEnabled = false
    }


}