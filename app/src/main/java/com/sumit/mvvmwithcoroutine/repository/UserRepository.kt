package com.sumit.mvvmwithcoroutine.repository

import androidx.lifecycle.LiveData
import com.sumit.mvvmwithcoroutine.api.CustomRetrofitBuilder
import com.sumit.mvvmwithcoroutine.model.User
import kotlinx.coroutines.*

// Singleton repository class

object UserRepository {

    var job: CompletableJob? = null

    fun getUsers(): LiveData<List<User>> {

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
    }

    fun getUser(userId: String): LiveData<User> {

        job = Job()

        return object : LiveData<User>() {
            override fun onActive() {
                super.onActive()

                job?.let { fetchUserJob ->
                    CoroutineScope(Dispatchers.IO + fetchUserJob).launch {

                        try {
                            val user: User? = CustomRetrofitBuilder.userService.getUser(userId)

                            if (user != null) {
                                withContext(Dispatchers.Main) {
                                    value = user
                                    fetchUserJob.complete()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            fetchUserJob.complete()

                            // Probably a network issue, handle gracefully later :)
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