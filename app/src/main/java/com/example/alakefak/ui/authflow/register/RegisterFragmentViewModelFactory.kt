package com.example.alakefak.ui.authflow.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.UserDatabase

class RegisterFragmentViewModelFactory(
    private val database: UserDatabase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterFragmentViewModel::class.java)) {
            return RegisterFragmentViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

