package com.lukitor.myapplicationC.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.lukitor.myapplicationC.data.room.entity.Users

@Database(entities = [Users::class],version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): UserDatabase =
           INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "Users.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
    }
}