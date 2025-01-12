package com.buller.binchecker.data.repositories

import com.buller.binchecker.data.local.LocalDataSource
import com.buller.binchecker.domain.repositories.LocalRepository
import com.buller.binchecker.domain.models.BinInfo

class LocalRepositoryImpl(private val source: LocalDataSource) : LocalRepository {

    override suspend fun setBinInfo(binInfo: BinInfo) {
        source.set(binInfo)
    }

    override suspend fun getAllBinInfo(): List<BinInfo> {
        return source.getAll()
    }
}