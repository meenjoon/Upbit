package com.mbj.upbit.di.viewmodel

import com.mbj.upbit.data.remote.network.repository.CoinInfoRepository
import com.mbj.upbit.feature.home.viewmodel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object MainViewModelProvider {

    @Provides
    @ViewModelScoped
    fun provideMainViewModel(coinInfoRepository: CoinInfoRepository) : MainViewModel {
        return MainViewModel(coinInfoRepository)
    }
}
