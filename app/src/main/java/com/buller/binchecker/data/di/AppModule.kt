package com.buller.binchecker.data.di

import com.buller.binchecker.ui.screens.history.HistoryViewModel
import com.buller.binchecker.ui.screens.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { HomeViewModel(getBinInfoUseCase = get(), setBinInfoToDatabaseUseCase = get()) }
    viewModel { HistoryViewModel(getAllBinInfoUseCase = get()) }
}