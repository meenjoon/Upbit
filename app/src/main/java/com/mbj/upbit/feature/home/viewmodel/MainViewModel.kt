package com.mbj.upbit.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.model.CoinInfoDetail
import com.mbj.upbit.data.remote.model.UpbitTickerResponse
import com.mbj.upbit.data.remote.network.adapter.ApiResultSuccess
import com.mbj.upbit.data.remote.network.repository.CoinInfoRepository
import com.mbj.upbit.data.remote.network.repository.UpbitWebSocketTickerManager
import com.mbj.upbit.data.remote.network.repository.UpbitWebSocketTickerRepository
import com.mbj.upbit.feature.home.common.UpbitWebSocketCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val coinInfoRepository: CoinInfoRepository
) : ViewModel(), UpbitWebSocketCallback {

    private lateinit var webSocketManager: UpbitWebSocketTickerRepository

    val coinInfoList: StateFlow<List<CoinInfo>> = getCoinInfoList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private val _upbitTickerResponses =
        MutableStateFlow<Map<String, UpbitTickerResponse>>(emptyMap())
    val upbitTickerResponses: StateFlow<Map<String, UpbitTickerResponse>> =
        _upbitTickerResponses.asStateFlow()

    private val _krwMarketString = MutableStateFlow("")
    private val krwMarketString: StateFlow<String> = _krwMarketString.asStateFlow()

    init {
        viewModelScope.launch {
            coinInfoList.collectLatest { response ->
                _krwMarketString.value = convertCoinInfoResponse(response)
            }
        }
        viewModelScope.launch {
            krwMarketString.collectLatest { krwMarket ->
                if (krwMarket.isNotBlank()) {
                    webSocketManager = UpbitWebSocketTickerRepository(
                        UpbitWebSocketTickerManager(
                            krwMarket,
                            this@MainViewModel
                        )
                    )
                    webSocketManager.startWebSocketConnection()
                }
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

    fun combineTickerAndCoinInfo(
        upbitTickerResponses: Map<String, UpbitTickerResponse>,
        coinInfoList: List<CoinInfo>
    ): List<CoinInfoDetail> {
        return upbitTickerResponses.mapNotNull { (code, tickerData) ->
            coinInfoList.find { it.market == code }?.let { coinInfo ->
                CoinInfoDetail(tickerData, coinInfo)
            }
        }
    }

    override fun onUpbitTickerResponseReceived(response: UpbitTickerResponse) {
        val updatedResponses = _upbitTickerResponses.value.toMutableMap()
        updatedResponses[response.code] = response
        _upbitTickerResponses.value = updatedResponses
    }

    override fun onCleared() {
        super.onCleared()
        webSocketManager.closeWebSocketConnection()
    }
}
