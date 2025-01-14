package com.buller.binchecker.data.local

import com.buller.binchecker.domain.models.BinInfo

interface LocalDataSource {
    suspend fun set(bin: String, binInfo: BinInfo)
    suspend fun getAll(): List<BinInfo>
}