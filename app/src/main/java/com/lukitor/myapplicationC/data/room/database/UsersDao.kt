package com.lukitor.myapplicationC.data.room.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lukitor.myapplicationC.data.room.entity.Users

@Dao
interface UsersDao {
    @Query("SELECT * FROM users")
    fun getUser(): LiveData<Users>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUsers(user : Users)

    @Update
    fun updateUser(user:Users)
}