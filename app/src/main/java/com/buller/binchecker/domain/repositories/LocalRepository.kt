package com.buller.binchecker.domain.repositories

import com.buller.binchecker.domain.models.BinInfo

interface LocalRepository {
    suspend fun setBinInfo(bin: String, binInfo: BinInfo)
    suspend fun getAllBinInfo(): List<BinInfo>
}