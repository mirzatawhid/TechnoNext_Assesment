package com.technonext.ltd.assesment.data.repository

import com.technonext.ltd.assesment.data.local.UserDao
import com.technonext.ltd.assesment.data.local.UserEntity
import javax.inject.Inject

class UserRepository @Inject constructor(private val userDao: UserDao) {

    suspend fun registerUser(email: String, password: String) {
        val user = UserEntity(email = email, password = password)
        userDao.insertUser(user)
    }

    suspend fun getUserByEmail(email: String): UserEntity? {
        return userDao.getUserByEmail(email)
    }
}
