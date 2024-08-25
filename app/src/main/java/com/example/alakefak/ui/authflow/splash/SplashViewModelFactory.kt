package com.example.alakefak.ui.authflow.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.ui.authflow.register.RegisterFragmentViewModel

class SplashViewModelFactory(
    private val database: UserDatabase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            return SplashViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}