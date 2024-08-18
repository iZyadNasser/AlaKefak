package com.example.alakefak.ui.authflow.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.AuthRepository
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.data.source.local.model.User
import kotlinx.coroutines.launch

class LoginFragmentViewModel(private val userDatabase: UserDatabase): ViewModel() {
    private val repository = AuthRepository(userDatabase.userDatabaseDao())

    private var _user = MutableLiveData<User?>(DEFAULT_USER_VALUE)
    val user: LiveData<User?>
        get() = _user

    fun handleUserData(email: String, password: String) {
        checkDataExistence(email, password)
    }

    private fun checkDataExistence(email: String, password: String) {
        viewModelScope.launch {
            _user.value = repository.getUserByEmailAndPassword(email, password)
        }
    }

    companion object {
        val DEFAULT_USER_VALUE = User(
            id = 0,
            email = "admin",
            userName = "admin",
            password = "admin"
        )
    }
}