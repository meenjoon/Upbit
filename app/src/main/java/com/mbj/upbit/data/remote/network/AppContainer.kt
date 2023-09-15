package com.mbj.upbit.data.remote.network

import com.mbj.upbit.data.remote.network.adapter.ApiClient

class AppContainer {

    private var apiClient: ApiClient? = null

    fun provideApiClient(): ApiClient {
        return apiClient ?: ApiClient.create().apply {
            apiClient = this
        }
    }
}
