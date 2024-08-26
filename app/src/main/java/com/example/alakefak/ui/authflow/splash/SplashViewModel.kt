package com.example.alakefak.ui.authflow.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.AuthRepository
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.data.source.local.model.User
import kotlinx.coroutines.launch

class SplashViewModel(db: UserDatabase) : ViewModel() {
    val database = db.userDatabaseDao()
    val repository = AuthRepository(database)

    private var _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    init {
        viewModelScope.launch {
            _users.value = repository.getAllUsers()
        }
    }
}