package com.buller.binchecker.data.local

import com.buller.binchecker.data.local.dao.BinInfoDao
import com.buller.binchecker.data.local.mappers.insert
import com.buller.binchecker.data.local.mappers.toBinInfo
import com.buller.binchecker.domain.models.BinInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomDataSource(private val binInfoDao: BinInfoDao) : LocalDataSource {
    override suspend fun set(bin: String, binInfo: BinInfo) {
        binInfo.insert(bin = bin, binInfoDao = binInfoDao)
    }

    override suspend fun getAll(): List<BinInfo> = withContext(Dispatchers.IO){
        binInfoDao.getAll().map {
            it.toBinInfo()
        }
    }
}