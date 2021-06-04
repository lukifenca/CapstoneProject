package com.lukitor.myapplicationC.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.lukitor.myapplicationC.data.room.database.UserDatabase
import com.lukitor.myapplicationC.data.room.database.UsersDao
import com.lukitor.myapplicationC.data.room.entity.Nutrients
import com.lukitor.myapplicationC.data.room.entity.Users
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository (application: Application){
    private val usersDao: UsersDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = UserDatabase.getInstance(application)
        usersDao = db.userDao()
    }

    fun getUser():LiveData<Users> = usersDao.getUser()
    fun insert(users: Users)= usersDao.insertUsers(users)
    fun update(users: Users)= usersDao.updateUser(users)
    fun getNutrient():LiveData<Nutrients> = usersDao.getNutrient()
    fun insertNutrient(nutrients: Nutrients)= usersDao.insertNutrient(nutrients)
    fun updateNutrient()= usersDao.updateNutrient()
}