package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.utilites.AppConstants
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST




private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(AppConstants.BASEURL)
    .build()



interface ExpenseMonitorApiService {
    @POST(AppConstants.USER_REGISTERATION)
    fun registerationUser(@Body userData: UserData): Deferred<UserResponse>
}


object ExpenseMonitorApi {
    val retrofitService : ExpenseMonitorApiService by lazy {
        retrofit.create(ExpenseMonitorApiService::class.java)
    }
}