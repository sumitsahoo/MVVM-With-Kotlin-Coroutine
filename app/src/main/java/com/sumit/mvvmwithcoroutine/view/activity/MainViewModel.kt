package com.sumit.mvvmwithcoroutine.view.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sumit.mvvmwithcoroutine.model.User
import com.sumit.mvvmwithcoroutine.repository.UserRepository

class MainViewModel: ViewModel(){

    private val _userId: MutableLiveData<String> = MutableLiveData()

    val user: LiveData<User> = Transformations.switchMap(_userId){
        UserRepository.getUser(it)
    }

    fun setUserId(userId: String){
        val userIdToFetch = userId

        if(_userId.value == userIdToFetch)
            return

        _userId.value = userIdToFetch
    }

    fun cancelJobs(){
        UserRepository.cancelJobs()
    }
}