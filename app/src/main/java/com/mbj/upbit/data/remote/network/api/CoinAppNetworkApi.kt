package com.mbj.upbit.data.remote.network.api

import com.mbj.upbit.data.remote.network.adapter.ApiResponse
import com.mbj.upbit.data.remote.model.CoinInfo
import kotlinx.coroutines.flow.Flow

interface CoinAppNetworkApi {
    fun getCoinInfoList(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit
    ): Flow<ApiResponse<List<CoinInfo>>>
}
