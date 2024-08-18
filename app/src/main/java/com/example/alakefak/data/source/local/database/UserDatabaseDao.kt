package com.example.alakefak.data.source.local.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.alakefak.data.source.local.model.User

@Dao
interface UserDatabaseDao {
    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun getUserByEmailAndPassword(email: String, password: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun doesEmailExist(email: String): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE user_name = :userName)")
    suspend fun doesUserNameExist(userName: String): Boolean
}