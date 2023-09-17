package com.mbj.upbit.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.network.adapter.ApiResultSuccess
import com.mbj.upbit.data.remote.network.repository.CoinInfoRepository
import com.mbj.upbit.data.remote.network.repository.UpbitWebSocketTickerManager
import com.mbj.upbit.data.remote.network.repository.UpbitWebSocketTickerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinInfoRepository: CoinInfoRepository
) : ViewModel() {

    private lateinit var webSocketTicker: UpbitWebSocketTickerRepository
    private val coinInfoList: StateFlow<List<CoinInfo>> = getCoinInfoList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            coinInfoList.collectLatest { response ->
                val formattedMarkets = convertCoinInfoResponse(response)

                webSocketTicker = UpbitWebSocketTickerRepository(UpbitWebSocketTickerManager(formattedMarkets))
                webSocketTicker.startWebSocketConnection()
            }
        }
    }

    private fun getCoinInfoList(): Flow<List<CoinInfo>> {
        return coinInfoRepository.getCoinInfoList(
            onComplete = { },
            onError = { }
        ).map { response ->
            when (response) {
                is ApiResultSuccess -> response.data
                else -> {
                    emptyList()
                }

            }
        }
    }

    private fun convertCoinInfoResponse(response: List<CoinInfo>): String {
        val krwMarkets = response
            .filter { it.market.startsWith("KRW-") }
            .map { it.market }
        return krwMarkets.joinToString(",")
    }

    override fun onCleared() {
        super.onCleared()
        webSocketTicker.closeWebSocketConnection()
    }
}
