package com.buller.binchecker.data.di

import com.buller.binchecker.data.repositories.BinRepositoryImpl
import com.buller.binchecker.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(getBinInfoUseCase = get()) }
}