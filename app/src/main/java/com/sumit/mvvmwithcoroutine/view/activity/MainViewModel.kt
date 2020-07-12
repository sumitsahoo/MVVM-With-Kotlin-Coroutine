package com.sumit.mvvmwithcoroutine.view.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sumit.mvvmwithcoroutine.model.Resource
import com.sumit.mvvmwithcoroutine.model.User
import com.sumit.mvvmwithcoroutine.repository.UserRepository

class MainViewModel : ViewModel() {

    private val _userId: MutableLiveData<String> = MutableLiveData()

    val user: LiveData<Resource<User>> = Transformations.switchMap(_userId) {
        UserRepository.getUser(it)
    }

    fun setUserId(userId: String) {
        if (_userId.value == userId)
            return

        _userId.value = userId
    }

    fun cancelJobs() {
        UserRepository.cancelJobs()
    }
}