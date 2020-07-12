package com.sumit.mvvmwithcoroutine.repository

import androidx.lifecycle.LiveData
import com.sumit.mvvmwithcoroutine.api.CustomRetrofitBuilder
import com.sumit.mvvmwithcoroutine.model.Resource
import com.sumit.mvvmwithcoroutine.model.User
import kotlinx.coroutines.*

// Singleton repository class

object UserRepository {

    var job: CompletableJob? = null

    /*fun getUsers(): LiveData<List<User>> {

        job = Job()

        return object : LiveData<List<User>>() {
            override fun onActive() {
                super.onActive()

                job?.let { fetchUserJob ->
                    CoroutineScope(Dispatchers.IO + fetchUserJob).launch {

                        val users = CustomRetrofitBuilder.userService.getUsers()

                        withContext(Dispatchers.Main) {
                            value = users
                            fetchUserJob.complete()
                        }
                    }
                }
            }
        }
    }*/

    fun getUser(userId: String): LiveData<Resource<User>> {

        job = Job()

        return object : LiveData<Resource<User>>() {
            override fun onActive() {
                super.onActive()

                job?.let { fetchUserJob ->
                    CoroutineScope(Dispatchers.IO + fetchUserJob).launch {

                        postValue(Resource.Loading(null))

                        try {
                            val user = CustomRetrofitBuilder.userService.getUser(userId)

                            postValue(Resource.Success(user))
                            fetchUserJob.complete()
                        } catch (e: Exception) {
                            // Probably a network issue, handle gracefully :)
                            postValue(Resource.Error(e))
                            fetchUserJob.complete()
                        }
                    }
                }
            }

        }
    }

    fun cancelJobs() {
        job?.cancel()
    }

}