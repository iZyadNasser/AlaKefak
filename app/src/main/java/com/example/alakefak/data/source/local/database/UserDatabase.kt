package com.example.alakefak.data.source.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class UserDatabase : RoomDatabase() {
    abstract fun userDatabaseDao(): UserDatabaseDao
}