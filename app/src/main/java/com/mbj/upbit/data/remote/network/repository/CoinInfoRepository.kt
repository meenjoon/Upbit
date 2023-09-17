package com.mbj.upbit.data.remote.network.repository

import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.network.adapter.ApiResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CoinInfoRepository @Inject constructor(
    private val coinInfoDataSource: CoinInfoDataSource
) {

    fun getCoinInfoList(
        onComplete: () -> Unit,
        onError: (message: String?) -> Unit
    ): Flow<ApiResponse<List<CoinInfo>>> {
        return coinInfoDataSource.getCoinInfoList(onComplete, onError)
    }
}
