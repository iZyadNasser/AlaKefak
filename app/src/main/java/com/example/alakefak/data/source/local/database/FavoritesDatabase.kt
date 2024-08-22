package com.example.alakefak.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.alakefak.data.source.local.model.FavoritesInfo

@Database(entities = [FavoritesInfo::class], version = 2)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDatabaseDao(): FavoritesDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: FavoritesDatabase? = null
        fun getDatabase(context: Context): FavoritesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java,
                    "favorites_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}