package com.expensemoitor.expensemonitor.Network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


private const val BASE_URL = "http://10.0.2.2:3000"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()



interface ExpenseMonitorApiService {
    @POST("/User/registeruser")
    fun registerationUser(@Body userData: UserData): Call<UserResponse>
}


object ExpenseMonitorApi {
    val retrofitService : ExpenseMonitorApiService by lazy {
        retrofit.create(ExpenseMonitorApiService::class.java)
    }
}