package com.buller.binchecker.data.di

import com.buller.binchecker.domain.usecases.GetBinInfoUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        GetBinInfoUseCase(repository = get())
    }
}