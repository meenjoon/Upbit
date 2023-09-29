package com.mbj.upbit.data.remote.network.adapter

import com.mbj.upbit.BuildConfig
import com.mbj.upbit.data.remote.model.CoinInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

interface ApiClient {

    @GET("v1/market/all")
    suspend fun getAllMarketInfo(): ApiResponse<List<CoinInfo>>

    companion object {

        fun create(): ApiClient {
            val logger = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            val moshi: Moshi = Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .build()

            return Retrofit.Builder()
                .baseUrl("https://api.upbit.com/")
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(ApiCallAdapterFactory.create())
                .build()
                .create(ApiClient::class.java)
        }
    }
}
