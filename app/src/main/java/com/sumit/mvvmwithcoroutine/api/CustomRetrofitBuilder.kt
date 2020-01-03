package com.sumit.mvvmwithcoroutine.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Singleton Retrofit instance

object CustomRetrofitBuilder {

    const val BASE_URL: String = "https://5de620aa9c4220001405b262.mockapi.io/api/v1/"

    val retrofitBuilder: Retrofit.Builder by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(UnsafeOkHttpClient.getUnsafeOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val userService: UserService by lazy {
        retrofitBuilder.build()
            .create(UserService::class.java)
    }
}