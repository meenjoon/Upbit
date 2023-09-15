package com.mbj.upbit.feature.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.network.adapter.ApiResultSuccess
import com.mbj.upbit.data.remote.network.repository.CoinInfoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val coinInfoRepository: CoinInfoRepository
) : ViewModel() {

    private val coinInfoList: StateFlow<List<CoinInfo>> = getCoinInfoList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            coinInfoList.collectLatest { response ->
                val formattedMarkets = convertCoinInfoResponse(response)

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

    fun convertCoinInfoResponse(response: List<CoinInfo>): String {
        val krwMarkets = response
            .filter { it.market.startsWith("KRW-") }
            .map { it.market }
        return krwMarkets.joinToString(",")
    }
}

class MainViewModelFactory(private val coinInfoRepository: CoinInfoRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(coinInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
