package com.mbj.upbit.data.remote.network.repository

import android.util.Log
import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.network.adapter.ApiClient
import com.mbj.upbit.data.remote.network.adapter.ApiResponse
import com.mbj.upbit.data.remote.network.adapter.onError
import com.mbj.upbit.data.remote.network.adapter.onException
import com.mbj.upbit.data.remote.network.adapter.onSuccess
import com.mbj.upbit.data.remote.network.api.CoinAppNetworkApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion

class CoinInfoDataSource(private val apiClient: ApiClient) : CoinAppNetworkApi {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default

    override fun getCoinInfoList(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit
    ): Flow<ApiResponse<List<CoinInfo>>> = flow {
        try {
            val response = apiClient.getAllMarketInfo()

            response.onSuccess {
                emit(response)
                Log.d("@@33 : ", "성공했음")
            }.onError { code, message ->
                onError("code: $code, message: $message")
                Log.d("@@33 : ", "실패 했음 code: $code, message: $message")
            }.onException {
                onError(it.message)
                Log.d("@@33 : ", "이셉션111 : ${it.message}")
            }
        } catch (e: Exception) {
            onError(e.message)
            Log.d("@@33 : ", "이셉션222 : ${e.message}")
        }
    }.onCompletion {
        onComplete()
    }.flowOn(defaultDispatcher)
}
