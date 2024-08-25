package com.example.alakefak.ui.authflow.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.alakefak.data.repository.AuthRepository
import com.example.alakefak.data.source.local.database.UserDatabase
import com.example.alakefak.data.source.local.model.User
import com.example.alakefak.ui.authflow.ErrorStates
import kotlinx.coroutines.launch

class RegisterFragmentViewModel(private val userDatabase: UserDatabase) : ViewModel() {
    private val repository = AuthRepository(userDatabase.userDatabaseDao())
    lateinit var user: User

    private var _errorState = MutableLiveData(ErrorStates.UNDEFINED)
    val errorState: LiveData<ErrorStates>
        get() = _errorState

    fun handleUserData(user: User) {
        this.user = user
        checkDataExistence()
        addData()
    }

    private fun checkDataExistence() {
        viewModelScope.launch {
            if (repository.doesEmailExist(user.email) && repository.doesUserNameExist(user.userName)) {
                _errorState.value = ErrorStates.BOTH
            } else if (repository.doesEmailExist(user.email)) {
                _errorState.value = ErrorStates.EMAIL
            } else if (repository.doesUserNameExist(user.userName)) {
                _errorState.value = ErrorStates.USERNAME
            } else {
                _errorState.value = ErrorStates.NONE
            }
        }
    }

    private fun addData() {
        viewModelScope.launch {
            if (!repository.doesEmailExist(user.email) && !repository.doesUserNameExist(user.userName)) {
                repository.insertUser(user)
            }
        }
    }

}