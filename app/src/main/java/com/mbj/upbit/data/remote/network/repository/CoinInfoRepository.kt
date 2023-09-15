package com.mbj.upbit.data.remote.network.repository

import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.network.adapter.ApiResponse
import kotlinx.coroutines.flow.Flow

class CoinInfoRepository(
    private val coinInfoDataSource: CoinInfoDataSource
) {

    fun getCoinInfoList(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit
    ): Flow<ApiResponse<List<CoinInfo>>> {
        return coinInfoDataSource.getCoinInfoList(onComplete, onError)
    }
}
