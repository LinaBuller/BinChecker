package com.buller.binchecker.data.di

import com.buller.binchecker.data.local.LocalDatabase
import com.buller.binchecker.data.local.RoomDataSource
import com.buller.binchecker.data.repositories.BinInfoRepositoryImpl
import com.buller.binchecker.data.repositories.LocalRepositoryImpl
import com.buller.binchecker.data.local.LocalDataSource
import com.buller.binchecker.domain.repositories.BinInfoRepository
import com.buller.binchecker.domain.repositories.LocalRepository
import org.koin.dsl.module

val dataModule = module {
    single<BinInfoRepository> { BinInfoRepositoryImpl(api = get()) }
    single { LocalDatabase.newInstance(context = get()) }
    single {
        val database = get<LocalDatabase>()
        database.getDao()
    }
    single<LocalDataSource> {
        RoomDataSource(binInfoDao = get())
    }
    single<LocalRepository> {
        LocalRepositoryImpl(source = get())
    }
}