package com.example.alakefak.data.repository

import com.example.alakefak.data.source.local.database.UserDatabaseDao
import com.example.alakefak.data.source.local.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AuthRepository(private val userDatabaseDao: UserDatabaseDao) {
    fun insertUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDatabaseDao.insert(user)
        }
    }

    fun deleteUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDatabaseDao.delete(user)
        }
    }

    fun getUserByEmailAndPassword(email: String, password: String): User? {
        val userDeferred = CoroutineScope(Dispatchers.IO).async {
            userDatabaseDao.getUserByEmailAndPassword(email, password)
        }

        var user: User? = null

        CoroutineScope(Dispatchers.Default).launch {
            user = userDeferred.await()
        }

        return user
    }

    fun doesUserExist(user: User): Boolean {
        val ansDeferred = CoroutineScope(Dispatchers.IO).async {
            userDatabaseDao.doesUserExist(user.id)
        }

        var ans: Boolean = false

        CoroutineScope(Dispatchers.Default).launch {
            ans = ansDeferred.await()
        }

        return ans
    }
}