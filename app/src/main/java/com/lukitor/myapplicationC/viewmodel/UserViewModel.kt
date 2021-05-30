package com.lukitor.myapplicationC.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.lukitor.myapplicationC.data.UserRepository
import com.lukitor.myapplicationC.data.room.entity.Users

class UserViewModel(application: Application) : ViewModel()  {
    private val userRepository: UserRepository = UserRepository(application)

    fun insert(users: Users)= userRepository.insert(users)
    fun update(users: Users)= userRepository.update(users)

    fun getUser():LiveData<Users> = userRepository.getUser()


}