package com.mbj.upbit.feature.home.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mbj.upbit.data.remote.model.CoinInfo
import com.mbj.upbit.data.remote.model.CoinInfoDetail
import com.mbj.upbit.data.remote.model.FilterType
import com.mbj.upbit.data.remote.model.UpbitTickerResponse
import com.mbj.upbit.data.remote.network.adapter.ApiResultSuccess
import com.mbj.upbit.data.remote.network.repository.CoinInfoRepository
import com.mbj.upbit.data.remote.network.repository.UpbitWebSocketTickerManager
import com.mbj.upbit.data.remote.network.repository.UpbitWebSocketTickerRepository
import com.mbj.upbit.feature.home.common.UpbitWebSocketCallback
import com.mbj.upbit.feature.util.formatted.CoinInfoFormatter
import com.mbj.upbit.ui.theme.CustomColors.Companion.Blue300
import com.mbj.upbit.ui.theme.CustomColors.Companion.Grey500
import com.mbj.upbit.ui.theme.CustomColors.Companion.Orange700
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

    private val coinInfoList: StateFlow<List<CoinInfo>> = getCoinInfoList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private val _upbitTickerResponses = MutableStateFlow<Map<String, UpbitTickerResponse>>(emptyMap())
    private val upbitTickerResponses: StateFlow<Map<String, UpbitTickerResponse>> = _upbitTickerResponses.asStateFlow()

    private val _krwMarketString = MutableStateFlow("")
    private val krwMarketString: StateFlow<String> = _krwMarketString.asStateFlow()

    private val _combinedDataList = MutableStateFlow<List<CoinInfoDetail>>(emptyList())
    val combinedDataList: StateFlow<List<CoinInfoDetail>> = _combinedDataList.asStateFlow()

    private val _filterType = MutableStateFlow(FilterType.NORMAL)
    val filterType: StateFlow<FilterType> = _filterType

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = _isError

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
                    _combinedDataList.value = combineTickerAndCoinInfo(upbitTickerResponses.value, coinInfoList.value)
                }
            }
        }
        viewModelScope.launch {
            upbitTickerResponses.collectLatest { upbitTickerResponses ->
                filterType.collectLatest { filterType ->
                    _combinedDataList.value = applyFilterAndSort(filterType, upbitTickerResponses, coinInfoList.value)
                }
            }
        }
    }

    private fun getCoinInfoList(): Flow<List<CoinInfo>> {
        return coinInfoRepository.getCoinInfoList(
            onComplete = { _isLoading.value = false },
            onError = { _isError.value = true}
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

    private fun combineTickerAndCoinInfo(
        upbitTickerResponses: Map<String, UpbitTickerResponse>,
        coinInfoList: List<CoinInfo>
    ): List<CoinInfoDetail> {
        return upbitTickerResponses.mapNotNull { (code, tickerData) ->
            coinInfoList.find { it.market == code }?.let { coinInfo ->
                CoinInfoDetail(tickerData, coinInfo)
            }
        }
    }

    fun calculateBorderInfo(
        currentPrice: Double,
        previousPrice: Double
    ): Pair<Dp, Color> {
        val priceChanged = currentPrice != previousPrice
        val borderThickness = if (priceChanged) 0.5.dp else 0.dp
        val borderColor = if (currentPrice > previousPrice) Orange700 else Blue300

        return Pair(borderThickness, borderColor)
    }

    private fun applyFilterAndSort(
        filterType: FilterType,
        upbitTickerResponses: Map<String, UpbitTickerResponse>,
        coinInfoList: List<CoinInfo>
    ): List<CoinInfoDetail> {
        return when (filterType) {
            FilterType.NORMAL -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
            FilterType.CURRENCY_PRICE_ASC -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
                .sortedBy { it.upbitTickerResponse.tradePrice }
            FilterType.CURRENCY_PRICE_DESC -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
                .sortedByDescending { it.upbitTickerResponse.tradePrice }
            FilterType.CHANGE_PERCENTAGE_ASC -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
                .sortedByDescending { CoinInfoFormatter.formatChangeRate(it.upbitTickerResponse.signedChangeRate) }
            FilterType.CHANGE_PERCENTAGE_DESC -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
                .sortedBy { CoinInfoFormatter.formatChangeRate(it.upbitTickerResponse.signedChangeRate) }
            FilterType.TRADING_VOLUME_ASC -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
                .sortedBy { it.upbitTickerResponse.tradeVolumeInKRW }
            FilterType.TRADING_VOLUME_DESC -> combineTickerAndCoinInfo(upbitTickerResponses, coinInfoList)
                .sortedByDescending { it.upbitTickerResponse.tradeVolumeInKRW }
        }
    }

    fun toggleCurrencyPriceFilter() {
        _filterType.value = when (_filterType.value) {
            FilterType.NORMAL -> FilterType.CURRENCY_PRICE_ASC
            FilterType.CURRENCY_PRICE_ASC -> FilterType.CURRENCY_PRICE_DESC
            FilterType.CURRENCY_PRICE_DESC -> FilterType.NORMAL
            else -> FilterType.CURRENCY_PRICE_ASC
        }
    }

    fun toggleChangePercentageFilter() {
        _filterType.value = when (_filterType.value) {
            FilterType.NORMAL -> FilterType.CHANGE_PERCENTAGE_ASC
            FilterType.CHANGE_PERCENTAGE_ASC -> FilterType.CHANGE_PERCENTAGE_DESC
            FilterType.CHANGE_PERCENTAGE_DESC -> FilterType.NORMAL
            else -> FilterType.CHANGE_PERCENTAGE_ASC
        }
    }

    fun toggleTradingVolumeFilter() {
        _filterType.value = when (_filterType.value) {
            FilterType.NORMAL -> FilterType.TRADING_VOLUME_ASC
            FilterType.TRADING_VOLUME_ASC -> FilterType.TRADING_VOLUME_DESC
            FilterType.TRADING_VOLUME_DESC -> FilterType.NORMAL
            else -> FilterType.TRADING_VOLUME_ASC
        }
    }

    fun getArrowIconBackground(filterType: Boolean): Color {
        return when (filterType) {
            true -> Blue300
            false -> Grey500
        }
    }

    fun isCurrencyPriceAsc(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.CURRENCY_PRICE_ASC -> true
            else -> false
        }
    }

    fun isCurrencyPriceDesc(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.CURRENCY_PRICE_DESC -> true
            else -> false
        }
    }

    fun isCurrencyPriceFilterActive(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.CURRENCY_PRICE_ASC -> true
            FilterType.CURRENCY_PRICE_DESC -> true
            else -> false
        }
    }

    fun isChangePercentageAsc(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.CHANGE_PERCENTAGE_ASC -> true
            else -> false
        }
    }

    fun isChangePercentageDesc(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.CHANGE_PERCENTAGE_DESC -> true
            else -> false
        }
    }

    fun isChangePercentageFilterActive(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.CHANGE_PERCENTAGE_ASC -> true
            FilterType.CHANGE_PERCENTAGE_DESC -> true
            else -> false
        }
    }

    fun isTradingVolumeAsc(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.TRADING_VOLUME_ASC -> true
            else -> false
        }
    }

    fun isTradingVolumeDesc(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.TRADING_VOLUME_DESC -> true
            else -> false
        }
    }

    fun isTradingVolumeFilterActive(filterType: FilterType): Boolean {
        return when (filterType) {
            FilterType.TRADING_VOLUME_ASC -> true
            FilterType.TRADING_VOLUME_DESC -> true
            else -> false
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
