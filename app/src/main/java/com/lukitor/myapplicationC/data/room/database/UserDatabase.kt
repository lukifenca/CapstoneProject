package com.lukitor.myapplicationC.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.lukitor.myapplicationC.data.room.entity.Nutrients
import com.lukitor.myapplicationC.data.room.entity.Users
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [Users::class, Nutrients::class],version = 1,exportSchema = false)
abstract class UserDatabase : RoomDatabase(){
    abstract fun userDao(): UsersDao

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        val passphrase: ByteArray = SQLiteDatabase.getBytes("Eunonia".toCharArray())
        val factory = SupportFactory(passphrase)
        @JvmStatic
        fun getInstance(context: Context): UserDatabase =
           INSTANCE ?: synchronized(this) {
                val instance = databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "Users.db"
                )
                    .allowMainThreadQueries().openHelperFactory(factory)
                    .build()
                INSTANCE = instance
                instance
            }
    }
}