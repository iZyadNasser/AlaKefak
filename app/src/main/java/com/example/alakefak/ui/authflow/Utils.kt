package com.example.alakefak.ui.authflow

import android.content.Context
import android.util.Patterns
import com.example.alakefak.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object Utils {
    const val INTENT_KEY = "user"
    fun validEmail(emailText: String, context: Context?): String? {
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            return context?.getString(R.string.invalidEmailAddress)
        }
        return null
    }

    fun validUsername(usernameText: String, context: Context?): String? {
        if (!usernameText.matches(".*[A-Z].*".toRegex()) && !usernameText.matches(".*[a-z].*".toRegex())) {
            return (context?.getString(R.string.minNumbersOfLetters))
        }
        return null
    }

    fun validPassword(passwordText: String, context: Context?): String? {
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

    fun validConfirmPassword(
        passwordText: String,
        confirmPasswordText: String,
        context: Context?
    ): String? {
        if (passwordText != confirmPasswordText) {
            return context?.getString(R.string.passwords_do_not_match)
        }
        return null
    }

    fun showSignOutDialog(
        context: Context,
        title: String,
        message: String,
        negativeButtonText: String,
        positiveButtonText: String,
        performSignOut: () -> Unit
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(positiveButtonText) { _, _ ->
                performSignOut()
            }
            .show()
    }
}

enum class ErrorStates() {
    UNDEFINED,
    NONE,
    EMAIL,
    USERNAME,
    BOTH
}