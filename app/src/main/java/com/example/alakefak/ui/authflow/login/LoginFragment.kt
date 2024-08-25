package com.example.alakefak.ui.authflow.login

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
import androidx.navigation.fragment.navArgs
import com.example.alakefak.R
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.databinding.FragmentLoginBinding
import com.example.alakefak.ui.appflow.RecipeActivity
import com.example.alakefak.ui.authflow.FormUtils

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginFragmentViewModel
    private val args: LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        val database = UserDatabase.getDatabase(requireContext().applicationContext)
        val viewModelFactory = LoginFragmentViewModelFactory(database)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginFragmentViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeFormState()
        setupTextWatchers()
        handleOnClicks()
        singInForm()
        binding.textFieldEmail.editText?.setText(args.email)
        binding.textFieldPassword.editText?.setText(args.password)
    }


    private fun observeFormState() {
        viewModel.user.observe(viewLifecycleOwner) {
            if (viewModel.user.value == null) {
                Toast.makeText(
                    this.context,
                    getString(R.string.incorrect_email_password_please_try_again),
                    Toast.LENGTH_LONG
                ).show()
            } else if (viewModel.user.value != LoginFragmentViewModel.DEFAULT_USER_VALUE) {
                val intent = Intent(activity, RecipeActivity::class.java)
                intent.putExtra(FormUtils.INTENT_KEY, viewModel.user.value)
                startActivity(intent)
                activity?.finish()
            }
        }
    }

    private fun setupTextWatchers() {
        val textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                singInForm()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        }

        binding.textFieldEmail.editText?.addTextChangedListener(textWatcher)
        binding.textFieldPassword.editText?.addTextChangedListener(textWatcher)
    }

    private fun handleOnClicks() {
        binding.loginBtn.setOnClickListener {
            viewModel.handleUserData(
                binding.textFieldEmail.editText?.text.toString(),
                binding.textFieldPassword.editText?.text.toString()
            )
            singInForm()
        }
    }

    private fun singInForm() {
        val email = binding.textFieldEmail.editText?.text.toString()
        val password = binding.textFieldPassword.editText?.text.toString()

        if (email.isEmpty()) {
            binding.textFieldEmail.helperText = getString(R.string.required)
        } else {
            binding.textFieldEmail.helperText = null
        }

        if (password.isEmpty()) {
            binding.textFieldPassword.helperText = getString(R.string.required)
        } else {
            binding.textFieldPassword.helperText = null
        }

        if (email.isNotEmpty() && password.isNotEmpty()) {
            signIn()
            binding.loginBtn.setBackgroundColor(resources.getColor(R.color.main_color))
        } else {
            disableSignIn()
            binding.loginBtn.setBackgroundColor(resources.getColor(R.color.button_disabled_color))
        }
    }


    private fun signIn() {
        binding.loginBtn.isEnabled = true
    }

    private fun disableSignIn() {
        binding.loginBtn.isEnabled = false
    }
}
