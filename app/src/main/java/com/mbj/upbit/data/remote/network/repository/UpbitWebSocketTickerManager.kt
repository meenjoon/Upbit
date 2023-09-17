package com.mbj.upbit.data.remote.network.repository

import android.util.Log
import com.mbj.upbit.BuildConfig
import com.mbj.upbit.data.remote.model.UpbitTickerResponse
import com.mbj.upbit.data.remote.network.api.WebSocketManager
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.nio.charset.StandardCharsets
import java.util.UUID

class UpbitWebSocketTickerManager(private val krwMarkets: String) : WebSocketManager {
    private val NORMAL_CLOSURE_STATUS = 1000
    private lateinit var webSocket: WebSocket

    override fun startWebSocketConnection() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("${BuildConfig.UPBIT_BASE_URL}websocket/v1")
            .build()

        webSocket = client.newWebSocket(request, UpbitWebSocketListener())
    }

    override fun closeWebSocketConnection() {
        webSocket.close(NORMAL_CLOSURE_STATUS, null)
    }

    inner class UpbitWebSocketListener : WebSocketListener() {
        override fun onOpen(webSocket: WebSocket, response: Response) {
            val uniqueTicket = UUID.randomUUID().toString()
            val json = """[{"ticket":"$uniqueTicket"},{"type":"ticker","codes":[$krwMarkets]}]"""
            webSocket.send(json)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d("Received text: ", text)
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            Log.d("Received bytes: ", bytes.hex())

            val jsonString = bytes.string(StandardCharsets.UTF_8)
            Log.d("Received json: ", jsonString)

            val upbitTickerResponse = jsonString.fromJsonToUpbitTickerResponse()
            if (upbitTickerResponse != null) {
                Log.d("Parsed response: ", "$upbitTickerResponse")
            } else {
                Log.d("Failed to parse response", "")
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            webSocket.cancel()
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            Log.d("Socket Closed :", "$code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            Log.d("Socket Error :", "${t.message}")
        }
    }

    fun String.fromJsonToUpbitTickerResponse(): UpbitTickerResponse? {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val adapter: JsonAdapter<UpbitTickerResponse> = moshi.adapter(UpbitTickerResponse::class.java)
        return adapter.fromJson(this)
    }
}

