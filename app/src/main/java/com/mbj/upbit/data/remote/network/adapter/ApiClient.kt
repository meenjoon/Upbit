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
}
