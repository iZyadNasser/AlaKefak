package com.example.alakefak.data.repository

import com.example.alakefak.data.source.local.database.UserDatabaseDao
import com.example.alakefak.data.source.local.model.User

class AuthRepository(private val userDatabaseDao: UserDatabaseDao) {
    suspend fun insertUser(user: User) {
        userDatabaseDao.insert(user)
    }

    suspend fun deleteUser(user: User) {
        userDatabaseDao.delete(user)
    }

    suspend fun getUserByEmailAndPassword(email: String, password: String): User? {
        return userDatabaseDao.getUserByEmailAndPassword(email, password)
    }

    suspend fun doesEmailExist(email: String): Boolean {
        return userDatabaseDao.doesEmailExist(email)
    }

    suspend fun doesUserNameExist(userName: String): Boolean {
        return userDatabaseDao.doesUserNameExist(userName)
    }

    suspend fun getAllUsers(): List<User> {
        return userDatabaseDao.getAllUsers()
    }
}