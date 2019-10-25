package com.expensemoitor.expensemonitor.network

import com.expensemoitor.expensemonitor.BuildConfig
import com.expensemoitor.expensemonitor.utilites.MyApp
import com.expensemoitor.expensemonitor.utilites.PrefManager
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitFactory {

    private val authInterceptor = Interceptor {chain->
        val newUrl = chain.request().url
            .newBuilder()
            .build()

        val newRequest = chain.request()
            .newBuilder()
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization","Bearer "+MyApp.context?.let { PrefManager.getAccessToken(it) })
            .addHeader("Connection","close")
            .url(newUrl)
            .build()

        chain.proceed(newRequest)
    }

    private val loggingInterceptor =  HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //Not logging the authkey if not debug
    private val client =
        if(BuildConfig.DEBUG){
            OkHttpClient().newBuilder()
                .addInterceptor(authInterceptor)
                .addInterceptor(loggingInterceptor)
                .build()
        }else{
            OkHttpClient().newBuilder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(authInterceptor)
                .retryOnConnectionFailure(true)
                .build()
        }


    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()


    fun retrofit(baseUrl : String) : Retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(baseUrl)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()




}