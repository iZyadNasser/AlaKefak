package com.example.alakefak.data.source.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class UserDatabase : RoomDatabase() {
    abstract fun userDatabaseDao(): UserDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getInstance(context: Context): UserDatabase {
            return INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                UserDatabase::class.java,
                "users_database"
            )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
        }
    }
}