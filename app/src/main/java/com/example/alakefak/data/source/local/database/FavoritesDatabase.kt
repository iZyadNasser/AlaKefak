package com.example.alakefak.data.source.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.alakefak.data.source.local.model.FavoritesInfo

@Database(entities = [FavoritesInfo::class], version = 1)
abstract class FavoritesDatabase : RoomDatabase() {
    abstract fun favoritesDatabaseDao(): FavoritesDatabaseDao

    companion object {
        @Volatile
        private var instance: FavoritesDatabase? = null

        fun getInstance(context: Context): FavoritesDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavoritesDatabase::class.java,
                    "favorites_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }
}