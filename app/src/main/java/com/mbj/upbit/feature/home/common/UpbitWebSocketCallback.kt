package com.mbj.upbit.feature.home.common

import com.mbj.upbit.data.remote.model.UpbitTickerResponse

interface UpbitWebSocketCallback {
    fun onUpbitTickerResponseReceived(response: UpbitTickerResponse)
}
