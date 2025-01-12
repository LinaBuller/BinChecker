package com.buller.binchecker.data.di

import com.buller.binchecker.domain.usecases.GetAllBinInfoUseCase
import com.buller.binchecker.domain.usecases.GetBinInfoUseCase
import com.buller.binchecker.domain.usecases.SetBinInfoToDatabaseUseCase
import org.koin.dsl.module

val domainModule = module {
    single {
        GetBinInfoUseCase(repository = get())
    }

    single {
        GetAllBinInfoUseCase(repository = get())
    }

    single {
        SetBinInfoToDatabaseUseCase(repository = get())
    }
}