package com.mbj.upbit.data.remote.network.repository

import com.mbj.upbit.data.remote.network.api.WebSocketManager

class UpbitWebSocketTickerRepository(private val webSocketManager: WebSocketManager) {
    fun startWebSocketConnection() {
        webSocketManager.startWebSocketConnection()
    }

    fun closeWebSocketConnection() {
        webSocketManager.closeWebSocketConnection()
    }
}
