package com.buller.binchecker.data.di

import com.buller.binchecker.data.repositories.BinRepositoryImpl
import com.buller.binchecker.domain.repositories.BinRepository
import org.koin.dsl.module

val dataModule = module{
    single<BinRepository> { BinRepositoryImpl(api = get()) }
}